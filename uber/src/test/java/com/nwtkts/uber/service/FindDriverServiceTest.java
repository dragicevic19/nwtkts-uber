package com.nwtkts.uber.service;

import com.nwtkts.uber.dto.RideRequest;
import com.nwtkts.uber.dto.RouteDTO;
import com.nwtkts.uber.model.*;
import com.nwtkts.uber.repository.DriverRepository;
import com.nwtkts.uber.repository.RideRepository;
import com.nwtkts.uber.service.impl.FindDriverServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class FindDriverServiceTest {

    @Mock
    private DriverRepository driverRepository;
    @Mock
    private RideRepository rideRepository;

    @InjectMocks
    private FindDriverServiceImpl findDriverService;


    @BeforeEach
    public void setUp() {

    }

    @Test
    @DisplayName("Should return NULL when there is no active drivers")
    public void shouldReturnNullWhenThereIsNoActiveDrivers() {
        Ride newRide = makeRide();

        Mockito.when(driverRepository.findAllByActive(true)).thenReturn(List.of());

        Driver actualDriver = findDriverService.searchDriver(newRide);
        Assertions.assertNull(actualDriver);
    }

    @Test
    @DisplayName("Should return NULL when there is active drivers but no one is available and also busy for next ride")
    public void shouldReturnNullWhenThereIsNoAvailableDriver() {
        Ride newRide = makeRide();

        Driver busyDriver = new Driver();
        busyDriver.setId(3L);
        busyDriver.setAvailable(false);
        busyDriver.setNextRideId(2L);

        Mockito.when(driverRepository.findAllByActive(true)).thenReturn(List.of(busyDriver));
        Mockito.when(driverRepository.findAllDetailedByAvailableAndBlocked(true, false))
                .thenReturn(List.of());
        Mockito.when(driverRepository.findAllDetailedByActiveAndAvailableAndBlocked(true, false, false))
                .thenReturn(List.of(busyDriver));


        Driver actualDriver = findDriverService.searchDriver(newRide);
        Assertions.assertNull(actualDriver);
    }

    private static List<List<DriverActivity>> provideDriverActivitiesWithMoreThan8Hours() {
        LocalDateTime now = LocalDateTime.now();
        List<DriverActivity> v1 = new ArrayList<>(List.of(
                new DriverActivity(now.minusHours(9), now.minusMinutes(30))));

        List<DriverActivity> v2 = new ArrayList<>(List.of(    // in last 24h -> 9h
                new DriverActivity(now.minusHours(28), now.minusHours(20))
                , new DriverActivity(now.minusHours(5), now)));


        List<DriverActivity> v3 = new ArrayList<>(List.of(
                new DriverActivity(now.minusHours(9), now.minusHours(9))));

        return new ArrayList<>(List.of(v1, v2, v3));
    }

    public static List<List<DriverActivity>> provideDriverActivitiesWithLessThan8Hours() {
        LocalDateTime now = LocalDateTime.now();
        List<DriverActivity> v1 = new ArrayList<>(List.of(
                new DriverActivity(now.minusHours(7), now.minusMinutes(30))));

        List<DriverActivity> v2 = new ArrayList<>(List.of(    // in last 24h -> 7h
                new DriverActivity(now.minusHours(28), now.minusHours(22))
                , new DriverActivity(now.minusHours(5), now)));


        List<DriverActivity> v3 = new ArrayList<>(List.of(
                new DriverActivity(now.minusHours(7), now.minusHours(7))));

        List<DriverActivity> v4 = new ArrayList<>(List.of(    // in last 24h -> 5h
                new DriverActivity(now.minusHours(34), now.minusHours(24))  // don't count
                , new DriverActivity(now.minusHours(5), now.minusHours(5))));

        return new ArrayList<>(List.of(v1, v2, v3, v4));
    }

    @ParameterizedTest
    @DisplayName("Should return NULL when there is active driver with more than 8 working hours")
    @MethodSource("provideDriverActivitiesWithMoreThan8Hours")
    public void shouldReturnNullWhenThereIsAvailableDriverWithLessWorkingHours(List<DriverActivity> driverActivity) {
        Ride newRide = makeRide();

        Driver driver = makeDriverAndVehicle();
        driver.setActivities(driverActivity);
        Mockito.when(driverRepository.findAllByActive(true)).thenReturn(List.of(driver));
        Mockito.when(driverRepository.findAllDetailedByAvailableAndBlocked(true, false))
                .thenReturn(List.of(driver));

        Driver actualDriver = findDriverService.searchDriver(newRide);
        Assertions.assertNull(actualDriver);
    }

    @ParameterizedTest
    @DisplayName("Should return driver when there is active driver with less than 8 working hours and driver is compatible with requests")
    @MethodSource("provideDriverActivitiesWithLessThan8Hours")
    public void shouldReturnNullWhenThereIsAvailableDriverWithMoreWorkingHours(List<DriverActivity> driverActivity) {
        Ride newRide = makeRide();

        Driver driver = makeDriverAndVehicle();
        driver.setActivities(driverActivity);

        Mockito.when(driverRepository.findAllByActive(true)).thenReturn(List.of(driver));

        Mockito.when(driverRepository.findAllDetailedByAvailableAndBlocked(true, false))
                .thenReturn(List.of(driver));

        Mockito.when(rideRepository.findAllDetailedByRideStatusAndDriver_Id(RideStatus.SCHEDULED, driver.getId()))
                .thenReturn(List.of());

        Driver actualDriver = findDriverService.searchDriver(newRide);
        Assertions.assertEquals(driver.getId(), actualDriver.getId());
    }

    public static List<Vehicle> provideVehiclesThatIsNotCompatibleWithRequests() {
        Vehicle wrongType = new Vehicle();
        wrongType.setType(new VehicleType(2L, "SUV", 2.));
        wrongType.setBabiesAllowed(true);
        wrongType.setPetsAllowed(true);

        Vehicle wrongBabies = new Vehicle();
        wrongBabies.setType(new VehicleType(1L, "SEDAN", 1.));
        wrongBabies.setBabiesAllowed(false);
        wrongBabies.setPetsAllowed(true);

        Vehicle wrongPets = new Vehicle();
        wrongPets.setType(new VehicleType(1L, "SEDAN", 1.));
        wrongPets.setBabiesAllowed(true);
        wrongPets.setPetsAllowed(false);

        return new ArrayList<>(List.of(wrongType, wrongBabies, wrongPets));
    }

    @ParameterizedTest
    @DisplayName("Should return NULL when driver is not compatible with requests")
    @MethodSource("provideVehiclesThatIsNotCompatibleWithRequests")
    public void shouldReturnNullWhenVehicleTypeIsNotCompatible(Vehicle notCompatibleVehicle) {
        Ride newRide = makeRide();

        Driver driver = makeDriverAndVehicle();
        driver.setVehicle(notCompatibleVehicle);

        Mockito.when(driverRepository.findAllByActive(true)).thenReturn(List.of(driver));

        Mockito.when(driverRepository.findAllDetailedByAvailableAndBlocked(true, false))
                .thenReturn(List.of(driver));

        Driver actualDriver = findDriverService.searchDriver(newRide);
        Assertions.assertNull(actualDriver);
    }

    @Test
    @DisplayName("Should return NULL when available driver is not making it before his next scheduled ride")
    public void shouldReturnNullWhenDriverIsNotMakingItBeforeScheduled() {
        Ride newRide = makeRide();

        Driver driver = makeDriverAndVehicle();

        Mockito.when(driverRepository.findAllByActive(true)).thenReturn(List.of(driver));

        Mockito.when(driverRepository.findAllDetailedByAvailableAndBlocked(true, false))
                .thenReturn(List.of(driver));

        Ride scheduledRide = new Ride();
        scheduledRide.setScheduledFor(LocalDateTime.now().plusMinutes(5));
        scheduledRide.setCalculatedDuration(8.);

        Mockito.when(rideRepository.findAllDetailedByRideStatusAndDriver_Id(RideStatus.SCHEDULED, driver.getId()))
                .thenReturn(List.of(scheduledRide));

        Driver actualDriver = findDriverService.searchDriver(newRide);
        Assertions.assertNull(actualDriver);
    }


    @Test
    @DisplayName("Should return driver when available driver is making it before his next scheduled ride")
    public void shouldReturnDriverWhenDriverIsMakingItBeforeScheduled() {
        Ride newRide = makeRide();

        Driver driver = makeDriverAndVehicle();

        Mockito.when(driverRepository.findAllByActive(true)).thenReturn(List.of(driver));

        Mockito.when(driverRepository.findAllDetailedByAvailableAndBlocked(true, false))
                .thenReturn(List.of(driver));

        Ride scheduledRide = new Ride();
        scheduledRide.setScheduledFor(LocalDateTime.now().plusHours(1));
        scheduledRide.setCalculatedDuration(8.);

        Mockito.when(rideRepository.findAllDetailedByRideStatusAndDriver_Id(RideStatus.SCHEDULED, driver.getId()))
                .thenReturn(List.of(scheduledRide));

        Driver actualDriver = findDriverService.searchDriver(newRide);
        Assertions.assertEquals(driver.getId(), actualDriver.getId());
    }


    @Test
    @DisplayName("Should return null when driver can't make new scheduled before his scheduled ride")
    public void shouldReturnNullWhenDriverIsNotMakingNewScheduledBeforeScheduled() {
        Ride newRide = makeRide();
        newRide.setScheduledFor(LocalDateTime.now().plusMinutes(30));

        Driver driver = makeDriverAndVehicle();

        Mockito.when(driverRepository.findAllByActive(true)).thenReturn(List.of(driver));

        Mockito.when(driverRepository.findAllDetailedByAvailableAndBlocked(true, false))
                .thenReturn(List.of(driver));

        Ride scheduledRide = new Ride();
        scheduledRide.setScheduledFor(LocalDateTime.now().plusMinutes(40));
        scheduledRide.setCalculatedDuration(8.);

        Mockito.when(rideRepository.findAllDetailedByRideStatusAndDriver_Id(RideStatus.SCHEDULED, driver.getId()))
                .thenReturn(List.of(scheduledRide));

        Driver actualDriver = findDriverService.searchDriver(newRide);
        Assertions.assertNull(actualDriver);
    }

    @Test
    @DisplayName("Should return null when driver can't make new scheduled after his scheduled ride")
    public void shouldReturnNullWhenDriverIsNotMakingNewScheduledAfterScheduled() {
        Ride newRide = makeRide();
        newRide.setScheduledFor(LocalDateTime.now().plusMinutes(50));

        Driver driver = makeDriverAndVehicle();

        Mockito.when(driverRepository.findAllByActive(true)).thenReturn(List.of(driver));

        Mockito.when(driverRepository.findAllDetailedByAvailableAndBlocked(true, false))
                .thenReturn(List.of(driver));

        Ride scheduledRide = new Ride();
        scheduledRide.setScheduledFor(LocalDateTime.now().plusMinutes(40));
        scheduledRide.setCalculatedDuration(8.);

        Mockito.when(rideRepository.findAllDetailedByRideStatusAndDriver_Id(RideStatus.SCHEDULED, driver.getId()))
                .thenReturn(List.of(scheduledRide));

        Driver actualDriver = findDriverService.searchDriver(newRide);
        Assertions.assertNull(actualDriver);
    }


    @Test
    @DisplayName("Should return driver when driver can make new scheduled before his scheduled ride")
    public void shouldReturnDriverWhenDriverIsMakingNewScheduledBeforeScheduled() {
        Ride newRide = makeRide();
        newRide.setScheduledFor(LocalDateTime.now().plusMinutes(10));

        Driver driver = makeDriverAndVehicle();

        Mockito.when(driverRepository.findAllByActive(true)).thenReturn(List.of(driver));

        Mockito.when(driverRepository.findAllDetailedByAvailableAndBlocked(true, false))
                .thenReturn(List.of(driver));

        Ride scheduledRide = new Ride();
        scheduledRide.setScheduledFor(LocalDateTime.now().plusMinutes(40));
        scheduledRide.setCalculatedDuration(8.);

        Mockito.when(rideRepository.findAllDetailedByRideStatusAndDriver_Id(RideStatus.SCHEDULED, driver.getId()))
                .thenReturn(List.of(scheduledRide));

        Driver actualDriver = findDriverService.searchDriver(newRide);
        Assertions.assertEquals(driver.getId(), actualDriver.getId());
    }


    @Test
    @DisplayName("Should return closer of two available drivers")
    public void shouldReturnCloserOfTwoAvailableDrivers() {
        Ride newRide = makeRide();
        Driver driver = makeDriverAndVehicle();

        Driver closerDriver = makeDriverAndVehicle();
        closerDriver.setId(4L);
        closerDriver.getVehicle().setCurrentLocation(new Location(45.2609635910084, 19.843166366367612));

        Mockito.when(driverRepository.findAllByActive(true)).thenReturn(List.of(driver, closerDriver));

        Mockito.when(driverRepository.findAllDetailedByAvailableAndBlocked(true, false))
                .thenReturn(List.of(driver, closerDriver));

        Mockito.when(rideRepository.findAllDetailedByRideStatusAndDriver_Id(Mockito.any(RideStatus.class), Mockito.anyLong()))
                .thenReturn(List.of());

        Driver actualDriver = findDriverService.searchDriver(newRide);
        Assertions.assertEquals(closerDriver.getId(), actualDriver.getId());

    }


    @Test
    @DisplayName("Should return available of two active drivers")
    public void shouldReturnAvailableOfTwoActiveDrivers() {
        Ride newRide = makeRide();
        Driver driver = makeDriverAndVehicle();
        driver.setAvailable(false);

        Driver availableDriver = makeDriverAndVehicle();
        availableDriver.setId(4L);
        availableDriver.getVehicle().setCurrentLocation(new Location(45.2609635910084, 19.843166366367612));

        Mockito.when(driverRepository.findAllByActive(true)).thenReturn(List.of(driver, availableDriver));

        Mockito.when(driverRepository.findAllDetailedByAvailableAndBlocked(true, false))
                .thenReturn(List.of(availableDriver));

        Mockito.when(rideRepository.findAllDetailedByRideStatusAndDriver_Id(Mockito.any(RideStatus.class), Mockito.anyLong()))
                .thenReturn(List.of());

        Driver actualDriver = findDriverService.searchDriver(newRide);
        Assertions.assertEquals(availableDriver.getId(), actualDriver.getId());

    }

    @Test
    @DisplayName("Should return driver closer to destination of two busy drivers")
    public void shouldReturnDriverCloserToDestination() {
        Ride newRide = makeRide();
        Driver expectedDriver = makeDriverAndVehicle();
        expectedDriver.setAvailable(false);

        Driver secondDriver = makeDriverAndVehicle();
        secondDriver.setId(4L);
        secondDriver.getVehicle().setCurrentLocation(new Location(45.2609635910084, 19.843166366367612));
        expectedDriver.setActive(false);

        Mockito.when(driverRepository.findAllByActive(true)).thenReturn(List.of(expectedDriver, secondDriver));

        Mockito.when(driverRepository.findAllDetailedByAvailableAndBlocked(true, false))
                .thenReturn(List.of());

        Mockito.when(driverRepository.findAllDetailedByActiveAndAvailableAndBlocked(true, false, false))
                .thenReturn(List.of(expectedDriver, secondDriver));

        Mockito.when(rideRepository.findAllDetailedByRideStatusAndDriver_Id(Mockito.any(RideStatus.class), Mockito.anyLong()))
                .thenReturn(List.of());

        Ride currentRide = makeRide();
        currentRide.setId(2L);

        Mockito.when(rideRepository.findByDriver_IdAndRideStatusIn(Mockito.anyLong(), Mockito.anyCollection()))
                .thenReturn(currentRide);

        Driver actualDriver = findDriverService.searchDriver(newRide);
        Assertions.assertEquals(expectedDriver.getId(), actualDriver.getId());
        Assertions.assertEquals(newRide.getId(), expectedDriver.getNextRideId());
    }

    private Driver makeDriverAndVehicle() {
        Driver driver = new Driver();
        driver.setId(3L);
        driver.setAvailable(true);
        LocalDateTime validActivityDateTime = LocalDateTime.now().minusHours(2);
        driver.setActivities(List.of(new DriverActivity(validActivityDateTime, validActivityDateTime)));
        Vehicle compatibleVehicle = new Vehicle();
        compatibleVehicle.setId(1L);
        compatibleVehicle.setType(new VehicleType(1L, "SEDAN", 1.));
        compatibleVehicle.setBabiesAllowed(true);
        compatibleVehicle.setPetsAllowed(true);
        compatibleVehicle.setCurrentLocation(new Location(45.234150, 19.834890));
        driver.setVehicle(compatibleVehicle);

        return driver;
    }

    private static Ride makeRide() {
        Ride newRide = new Ride();
        newRide.setId(1L);
        newRide.setScheduledFor(null);
        newRide.setStartingLocation(new Location(45.265405, 19.848011));
        newRide.setEndingLocation(new Location(45.246143, 19.844253));
        newRide.setRequestedVehicleType(new VehicleType(1L, "SEDAN", 1.));
        newRide.setBabiesOnRide(true);
        newRide.setPetsOnRide(true);
        newRide.setCalculatedDuration(8.);
        return newRide;
    }
}
