package com.nwtkts.uber.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwtkts.uber.dto.*;
import com.nwtkts.uber.exception.NotFoundException;
import com.nwtkts.uber.model.*;
import com.nwtkts.uber.repository.ClientRepository;
import com.nwtkts.uber.repository.DriverRepository;
import com.nwtkts.uber.repository.RideRepository;
import com.nwtkts.uber.repository.VehicleTypeRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.HashSet;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@AutoConfigureMockMvc
public class RideControllerTest {

    private String accessToken;
    private String clientAccessToken;
    private String driverAccessToken;
    private HttpHeaders headers;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public void login() {
        ResponseEntity<UserTokenState> responseEntity =
                restTemplate.postForEntity("/auth/login",
                        new JwtAuthenticationRequest("driver@gmail.com", "123"),
                        UserTokenState.class);
        UserTokenState userTokenState = responseEntity.getBody();
        accessToken = "Bearer " + userTokenState.getAccessToken();

        headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
    }

    private void loginAsClient() {
        ResponseEntity<UserTokenState> responseEntity =
                restTemplate.postForEntity("/auth/login",
                        new JwtAuthenticationRequest("user@gmail.com", "123"),
                        UserTokenState.class);
        UserTokenState userTokenState = responseEntity.getBody();
        clientAccessToken = "Bearer " + userTokenState.getAccessToken();
    }

    private void loginAsDriver() {
        ResponseEntity<UserTokenState> responseEntity =
                restTemplate.postForEntity("/auth/login",
                        new JwtAuthenticationRequest("seconddriver@gmail.com", "123"),
                        UserTokenState.class);
        UserTokenState userTokenState = responseEntity.getBody();
        driverAccessToken = "Bearer " + userTokenState.getAccessToken();
    }

    @Test
    @DisplayName("Should return 401 when user is not logged in")
    void shouldReturn404() throws Exception {
        Ride ride = createRide();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/driver/startRide")
                        .content(asJsonString(ride))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return 403 when logged in user is not driver and wants to start a ride.")
    void shouldReturn403() throws Exception {
        loginAsClient();
        Ride ride = createRide();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/driver/startRide")
                        .content(asJsonString(ride.getId()))
                        .header("Authorization", clientAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
    @Test
    @DisplayName("Should return Not Found Exception when ride ID isn't matching any ID in database.")
    void shouldThrowNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/driver/startRide")
                        .content(asJsonString(85L))
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Ride should return OK and have status: STARTED")
    void shouldReturnOKAndStarted() throws Exception {
        Ride ride = createRide();

        HttpEntity<Long> rideId = new HttpEntity<>(ride.getId(), headers);

        ResponseEntity<?> responseEntity =
                restTemplate.exchange("/driver/startRide", HttpMethod.POST, rideId, Void.class);

        Ride rideInDatabase = this.rideRepository.findById(ride.getId()).orElseThrow(NotFoundException::new);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(RideStatus.STARTED, rideInDatabase.getRideStatus());
    }

    @Test
    @DisplayName("Should throw BadRequest if RideStatus isn't WAITING_FOR_CLIENT before starting Ride")
    void shouldThrowBadRequest() throws Exception {
        Ride ride = createRide();
        ride.setRideStatus(RideStatus.ENDING);
        this.rideRepository.save(ride);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/driver/startRide")
                        .content(asJsonString(ride.getId()))
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should throw BadRequestException when client wants to report driver, but isn't in a ride.")
    void shouldThrowBadRequestException() throws Exception {
        loginAsClient();
        Driver activeDriver = setUpDriver(2L, true, false);
        Client client = this.clientRepository.findSummaryByEmail("seconduser@gmail.com");
        RideRequest rideRequest = makeRideRequest(false, false);
        Ride ride = makeRideForClientAndDriver(activeDriver, client, rideRequest);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/client/reportDriver")
                        .content(asJsonString(ride.getId()))
                        .header("Authorization", clientAccessToken)  //logged in as: user@gmail.com
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should throw NotFoundException when client wants to report driver from ride which not exist.")
    void shouldThrowNotFoundExceptionIfRideDoesNotExist() throws Exception {
        loginAsClient();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/client/reportDriver")
                        .content(asJsonString(86L))
                        .header("Authorization", clientAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should throw ForbiddenException when driver wants to report driver.")
    void shouldThrowForbiddenIfDriverWantsToReportDriver() throws Exception {
        loginAsDriver();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/client/reportDriver")
                        .content(asJsonString(86L))
                        .header("Authorization", driverAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    private Ride makeRideForClientAndDriver(Driver driver, Client client, RideRequest rideRequest) {
        Ride ride = new Ride(rideRequest, 1.3, rideRequest.getVehicleType());
        ride.setDriver(driver);
        ride.setVehicle(driver.getVehicle());
        ride.setClientsInfo(new HashSet<>(Arrays.asList(new ClientRide(client))));
        ride.setRideStatus(RideStatus.STARTED);
        return this.rideRepository.save(ride);
    }

    private Driver setUpDriver(Long driverId, boolean active, boolean available) {
        Driver driver = this.driverRepository.findById(driverId).orElseThrow(NotFoundException::new);
        driver.setActive(active);
        driver.setAvailable(available);
        return this.driverRepository.save(driver);
    }

    public Ride createRide() {
        RideRequest rideRequest = makeRideRequest(true, true);
        VehicleType vehicleType = vehicleTypeRepository.findById(2L).orElseThrow();
        Ride ride = new Ride(rideRequest, 3.6, vehicleType);
        ride.setRideStatus(RideStatus.WAITING_FOR_CLIENT);
        this.rideRepository.save(ride);
        return ride;
    }

    private RideRequest makeRideRequest(boolean pets, boolean babies) {
        RouteDTO routeDTO = new RouteDTO("[{\"steps\":[{\"geometry\":{\"coordinates\":[[19.848011,45.265405],[19.848056,45.265405],[19.848622,45.265447],[19.848683,45.265454],[19.848811,45.265477]],\"type\":\"LineString\"},\"maneuver\":{\"bearing_after\":90,\"bearing_before\":0,\"location\":[19.848011,45.265405],\"modifier\":\"right\",\"type\":\"depart\"},\"mode\":\"driving\",\"driving_side\":\"right\",\"name\":\"Беле Њиве\",\"intersections\":[{\"out\":0,\"entry\":[true],\"bearings\":[90],\"location\":[19.848011,45.265405]}],\"weight\":11.1,\"duration\":11.1,\"distance\":63.4},{\"geometry\":{\"coordinates\":[[19.848811,45.265477],[19.848986,45.264724],[19.849244,45.26382],[19.849259,45.263768]],\"type\":\"LineString\"},\"maneuver\":{\"bearing_after\":170,\"bearing_before\":81,\"location\":[19.848811,45.265477],\"modifier\":\"right\",\"type\":\"turn\"},\"mode\":\"driving\",\"driving_side\":\"right\",\"name\":\"Ђорђа Рајковића\",\"intersections\":[{\"out\":1,\"in\":2,\"entry\":[true,true,false,true],\"bearings\":[60,165,255,345],\"location\":[19.848811,45.265477]},{\"out\":0,\"in\":2,\"entry\":[true,true,false],\"bearings\":[165,270,345],\"location\":[19.848986,45.264724]}],\"weight\":32.2,\"duration\":32.2,\"distance\":193.2},{\"geometry\":{\"coordinates\":[[19.849259,45.263768],[19.850386,45.263823]],\"type\":\"LineString\"},\"maneuver\":{\"bearing_after\":85,\"bearing_before\":167,\"location\":[19.849259,45.263768],\"modifier\":\"left\",\"type\":\"turn\"},\"mode\":\"driving\",\"driving_side\":\"right\",\"name\":\"Јаше Игњатовића\",\"intersections\":[{\"out\":0,\"in\":3,\"entry\":[true,true,true,false],\"bearings\":[90,180,270,345],\"location\":[19.849259,45.263768]}],\"weight\":10.6,\"duration\":10.6,\"distance\":88.6},{\"geometry\":{\"coordinates\":[[19.850386,45.263823],[19.850359,45.262965],[19.850355,45.262808],[19.850319,45.261857],[19.850309,45.261577],[19.850292,45.261461]],\"type\":\"LineString\"},\"maneuver\":{\"bearing_after\":180,\"bearing_before\":85,\"location\":[19.850386,45.263823],\"modifier\":\"right\",\"type\":\"turn\"},\"mode\":\"driving\",\"driving_side\":\"right\",\"name\":\"Косовска\",\"intersections\":[{\"out\":2,\"in\":3,\"entry\":[true,true,true,false],\"bearings\":[0,60,180,270],\"location\":[19.850386,45.263823]},{\"out\":2,\"in\":0,\"entry\":[false,true,true],\"bearings\":[0,90,180],\"location\":[19.850359,45.262965]},{\"out\":2,\"in\":0,\"entry\":[false,true,true,true],\"bearings\":[0,90,180,270],\"location\":[19.850355,45.262808]}],\"weight\":31.9,\"duration\":31.9,\"distance\":262.6},{\"geometry\":{\"coordinates\":[[19.850292,45.261461],[19.850368,45.261466],[19.850585,45.261481],[19.850818,45.261497],[19.851544,45.261546],[19.851801,45.261564],[19.851862,45.261568],[19.853701,45.261694],[19.854222,45.261726],[19.854271,45.26173],[19.854321,45.261737],[19.854388,45.261756],[19.85444,45.261779],[19.854498,45.261796],[19.854556,45.261806],[19.854636,45.261813]],\"type\":\"LineString\"},\"maneuver\":{\"bearing_after\":84,\"bearing_before\":181,\"location\":[19.850292,45.261461],\"modifier\":\"left\",\"type\":\"turn\"},\"mode\":\"driving\",\"driving_side\":\"right\",\"name\":\"Марка Миљанова\",\"intersections\":[{\"out\":1,\"in\":0,\"entry\":[false,true,true,true],\"bearings\":[0,90,195,270],\"location\":[19.850292,45.261461]},{\"out\":0,\"in\":2,\"entry\":[true,false,false],\"bearings\":[90,195,270],\"location\":[19.851544,45.261546]},{\"out\":1,\"in\":2,\"entry\":[true,true,false],\"bearings\":[0,90,270],\"location\":[19.851801,45.261564]},{\"out\":0,\"in\":2,\"entry\":[true,true,false],\"bearings\":[60,90,270],\"location\":[19.854321,45.261737]},{\"out\":1,\"in\":2,\"entry\":[false,true,false],\"bearings\":[15,75,240],\"location\":[19.85444,45.261779]}],\"weight\":32.8,\"duration\":32.8,\"distance\":344.1},{\"geometry\":{\"coordinates\":[[19.854636,45.261813],[19.854691,45.261814]],\"type\":\"LineString\"},\"maneuver\":{\"exit\":1,\"bearing_after\":87,\"bearing_before\":75,\"location\":[19.854636,45.261813],\"modifier\":\"straight\",\"type\":\"roundabout\"},\"mode\":\"driving\",\"driving_side\":\"right\",\"name\":\"\",\"intersections\":[{\"out\":0,\"in\":1,\"entry\":[true,false,false],\"bearings\":[90,255,285],\"location\":[19.854636,45.261813]}],\"weight\":0.4,\"duration\":0.4,\"distance\":4.3},{\"geometry\":{\"coordinates\":[[19.854691,45.261814],[19.854775,45.261788],[19.854847,45.261759],[19.855097,45.261635],[19.855181,45.261597],[19.855964,45.261268],[19.856117,45.261204]],\"type\":\"LineString\"},\"maneuver\":{\"exit\":1,\"bearing_after\":120,\"bearing_before\":87,\"location\":[19.854691,45.261814],\"modifier\":\"slight right\",\"type\":\"exit roundabout\"},\"mode\":\"driving\",\"driving_side\":\"right\",\"name\":\"\",\"intersections\":[{\"out\":1,\"in\":2,\"entry\":[true,true,false],\"bearings\":[75,120,270],\"location\":[19.854691,45.261814]},{\"out\":0,\"in\":2,\"entry\":[true,false,false],\"bearings\":[121,295,305],\"location\":[19.855097,45.261635]}],\"weight\":13.3,\"duration\":13.3,\"distance\":131},{\"geometry\":{\"coordinates\":[[19.856117,45.261204],[19.856026,45.261068],[19.855931,45.260915],[19.855824,45.260724],[19.855726,45.260541],[19.855649,45.260402],[19.855574,45.260246],[19.855481,45.26002],[19.855326,45.259626],[19.855262,45.259463],[19.855058,45.258942],[19.854968,45.258687],[19.854892,45.258439],[19.854841,45.258246],[19.854807,45.258086],[19.854775,45.257908],[19.854741,45.25758],[19.85473,45.257296],[19.854729,45.257263],[19.854727,45.257227],[19.854727,45.257222],[19.854726,45.25719],[19.854734,45.25695],[19.854761,45.256727],[19.854787,45.256464],[19.854811,45.256296],[19.854821,45.25623],[19.854825,45.256214],[19.85483,45.256184],[19.854896,45.255803],[19.854956,45.255506],[19.854963,45.255471],[19.854989,45.255343],[19.854997,45.255299],[19.855139,45.254522],[19.855276,45.253706],[19.855304,45.253539]],\"type\":\"LineString\"},\"maneuver\":{\"bearing_after\":203,\"bearing_before\":119,\"location\":[19.856117,45.261204],\"modifier\":\"right\",\"type\":\"turn\"},\"mode\":\"driving\",\"driving_side\":\"right\",\"name\":\"Београдски кеј\",\"intersections\":[{\"out\":2,\"in\":3,\"entry\":[false,true,true,false],\"bearings\":[30,90,210,300],\"location\":[19.856117,45.261204]},{\"out\":2,\"in\":0,\"entry\":[false,true,true,true],\"bearings\":[15,105,195,270],\"location\":[19.855262,45.259463]},{\"out\":2,\"in\":0,\"entry\":[false,true,true,false],\"bearings\":[0,90,180,270],\"location\":[19.854775,45.257908]},{\"out\":2,\"in\":0,\"entry\":[false,true,true,true],\"bearings\":[0,105,180,285],\"location\":[19.854727,45.257227]},{\"out\":2,\"in\":0,\"entry\":[false,true,true,false],\"bearings\":[0,75,180,270],\"location\":[19.854825,45.256214]},{\"out\":1,\"in\":3,\"entry\":[true,true,true,false],\"bearings\":[90,180,240,345],\"location\":[19.854997,45.255299]}],\"weight\":60.799999999,\"duration\":60.799999999,\"distance\":870.8},{\"geometry\":{\"coordinates\":[[19.855304,45.253539],[19.855325,45.253396],[19.855451,45.252529],[19.855456,45.252493],[19.855481,45.252306],[19.855501,45.252152],[19.855571,45.251612],[19.855579,45.251555],[19.855594,45.251442],[19.855644,45.251052],[19.855647,45.251021],[19.855704,45.250527],[19.855705,45.250518],[19.855706,45.250496],[19.855705,45.250415],[19.8557,45.250346],[19.855687,45.250276],[19.855667,45.250224],[19.855625,45.250119],[19.855585,45.250034]],\"type\":\"LineString\"},\"maneuver\":{\"bearing_after\":172,\"bearing_before\":172,\"location\":[19.855304,45.253539],\"modifier\":\"straight\",\"type\":\"new name\"},\"mode\":\"driving\",\"driving_side\":\"right\",\"name\":\"Кеј жртава рације\",\"intersections\":[{\"out\":2,\"in\":0,\"entry\":[false,true,true,true],\"bearings\":[0,90,180,270],\"location\":[19.855304,45.253539]},{\"out\":2,\"in\":0,\"entry\":[false,true,true,true],\"bearings\":[0,90,180,255],\"location\":[19.855481,45.252306]},{\"out\":2,\"in\":0,\"entry\":[false,true,true,true],\"bearings\":[0,75,180,255],\"location\":[19.855594,45.251442]},{\"out\":1,\"in\":0,\"entry\":[false,true,true],\"bearings\":[0,180,270],\"location\":[19.855704,45.250527]}],\"weight\":27.599999999,\"duration\":27.599999999,\"distance\":392.3},{\"geometry\":{\"coordinates\":[[19.855585,45.250034],[19.855547,45.249977],[19.855503,45.249916],[19.855443,45.249843],[19.855366,45.249774],[19.855304,45.249724],[19.855227,45.249666],[19.855125,45.249621],[19.85496,45.249559],[19.854461,45.249395],[19.85427,45.249339],[19.852673,45.248841],[19.852639,45.24883],[19.852493,45.248785],[19.852352,45.248742],[19.852313,45.248731],[19.851537,45.248481],[19.851474,45.248462],[19.851049,45.248334],[19.850756,45.248264],[19.850389,45.248164],[19.850177,45.248098],[19.849928,45.247986],[19.849704,45.247875],[19.849685,45.247865],[19.849646,45.247843],[19.84961,45.247822],[19.849482,45.247751],[19.849392,45.247704],[19.849151,45.247544],[19.849023,45.247454],[19.84892,45.24736],[19.848786,45.247234],[19.848073,45.246466],[19.848049,45.24644],[19.847934,45.246316],[19.847753,45.246133],[19.847506,45.245873],[19.847397,45.245777],[19.847313,45.245713],[19.846912,45.245454],[19.846798,45.245387],[19.846694,45.245333],[19.846655,45.245314],[19.846635,45.245304],[19.846598,45.245286],[19.846449,45.245228],[19.846158,45.24512],[19.845731,45.24497],[19.845131,45.244817],[19.845003,45.244785],[19.84496,45.244774]],\"type\":\"LineString\"},\"maneuver\":{\"bearing_after\":203,\"bearing_before\":198,\"location\":[19.855585,45.250034],\"modifier\":\"straight\",\"type\":\"new name\"},\"mode\":\"driving\",\"driving_side\":\"right\",\"name\":\"Булевар Цара Лазара\",\"intersections\":[{\"out\":1,\"in\":0,\"entry\":[false,true],\"bearings\":[15,210],\"location\":[19.855585,45.250034]},{\"out\":2,\"in\":0,\"entry\":[false,true,true,true],\"bearings\":[60,150,240,330],\"location\":[19.852493,45.248785]},{\"out\":2,\"in\":0,\"entry\":[false,false,true,true],\"bearings\":[60,135,240,330],\"location\":[19.849482,45.247751]},{\"out\":1,\"in\":0,\"entry\":[false,true,false],\"bearings\":[60,225,330],\"location\":[19.849392,45.247704]},{\"out\":1,\"in\":0,\"entry\":[false,true,true],\"bearings\":[30,210,300],\"location\":[19.847934,45.246316]},{\"out\":2,\"in\":0,\"entry\":[false,true,true],\"bearings\":[44,218,228],\"location\":[19.847313,45.245713]},{\"out\":2,\"in\":0,\"entry\":[false,false,true],\"bearings\":[60,75,255],\"location\":[19.845731,45.24497]}],\"weight\":93.5,\"duration\":93.5,\"distance\":1049.7},{\"geometry\":{\"coordinates\":[[19.84496,45.244774],[19.844916,45.24486],[19.844904,45.244881],[19.844846,45.244989],[19.844806,45.24507],[19.8447,45.245281],[19.844253,45.246143],[19.844238,45.246233],[19.844254,45.246409],[19.844257,45.246473],[19.844247,45.246568],[19.844082,45.246865],[19.843995,45.246919],[19.843951,45.246935],[19.84389,45.24694],[19.843826,45.246939],[19.843745,45.246928],[19.843693,45.246921],[19.843567,45.246894],[19.843483,45.246848],[19.843383,45.246759],[19.84268,45.246134]],\"type\":\"LineString\"},\"maneuver\":{\"bearing_after\":338,\"bearing_before\":248,\"location\":[19.84496,45.244774],\"modifier\":\"right\",\"type\":\"turn\"},\"mode\":\"driving\",\"driving_side\":\"right\",\"name\":\"\",\"intersections\":[{\"out\":2,\"in\":0,\"entry\":[false,true,true],\"bearings\":[75,255,345],\"location\":[19.84496,45.244774]},{\"out\":2,\"in\":0,\"entry\":[false,true,true],\"bearings\":[165,330,345],\"location\":[19.8447,45.245281]},{\"out\":0,\"in\":1,\"entry\":[true,false,true],\"bearings\":[0,165,240],\"location\":[19.844253,45.246143]}],\"weight\":109.9,\"duration\":95.5,\"distance\":398.3},{\"geometry\":{\"coordinates\":[[19.84268,45.246134],[19.84268,45.246134]],\"type\":\"LineString\"},\"maneuver\":{\"bearing_after\":0,\"bearing_before\":218,\"location\":[19.84268,45.246134],\"modifier\":\"left\",\"type\":\"arrive\"},\"mode\":\"driving\",\"driving_side\":\"right\",\"name\":\"\",\"intersections\":[{\"in\":0,\"entry\":[true],\"bearings\":[38],\"location\":[19.84268,45.246134]}],\"weight\":0,\"duration\":0,\"distance\":0}],\"summary\":\"Београдски кеј, Булевар Цара Лазара\",\"weight\":424.1,\"duration\":409.7,\"distance\":3798.2}]\t", 2., 2., 2., 45.265405, 19.848011, 45.246143, 19.844253, Arrays.asList("Bele njive 24, Novi Sad", "Promenada, Novi Sad"));
        VehicleType vehicleType = vehicleTypeRepository.findById(2L).orElseThrow();
        return new RideRequest(routeDTO, vehicleType, pets, babies, Arrays.asList(), 3.6, 3.6, null, Arrays.asList("Bele njive 24, Novi Sad", "Promenada, Novi Sad"));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
