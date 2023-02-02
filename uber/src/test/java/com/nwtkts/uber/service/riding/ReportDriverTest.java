package com.nwtkts.uber.service.riding;

import com.nwtkts.uber.exception.BadRequestException;
import com.nwtkts.uber.exception.NotFoundException;
import com.nwtkts.uber.model.*;
import com.nwtkts.uber.repository.MessageRepository;
import com.nwtkts.uber.repository.RideRepository;
import com.nwtkts.uber.service.impl.RideServiceImpl;
import com.nwtkts.uber.util.Utils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class ReportDriverTest {
    @Mock
    private RideRepository rideRepository;
    @Mock
    private MessageRepository messageRepository;
    @InjectMocks
    private RideServiceImpl rideService;
    private Client client;

    @BeforeEach
    public void setUp() {
        client = Utils.makeClient();
    }

    @Test
    @DisplayName("Should throw NotFoundException when ride doesn't exist in database.")
    public void NotFoundException_when_RideId_does_not_exists() {
        Long rideId = 1L;
        Mockito.when(rideRepository.findDetailedById(rideId))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> this.rideService.reportDriver(client, rideId));
    }

    @Test
    @DisplayName("Should throw BadRequestException when Client is not in Ride but wants to report Driver")
    public void BadRequestException_when_not_allowed_client_wants_to_report_driver(){
        Long rideId = 1L;

        Ride mockRide = new Ride();

        Client mockClient = new Client();
        mockClient.setId(1L);

        ClientRide clientRide = new ClientRide(mockClient);
        clientRide.setClient(mockClient);

        Set<ClientRide> clientRides = new HashSet<>();
        clientRides.add(clientRide);

        mockRide.setClientsInfo(clientRides);

        Mockito.when(rideRepository.findDetailedById(rideId))
                .thenReturn(Optional.of(mockRide));

        Client mockClient2 = new Client();
        mockClient2.setId(33L);

        Assertions.assertThrows(BadRequestException.class, () -> this.rideService.reportDriver(mockClient2, rideId)); //clientId: 1
    }

    @Test
    @DisplayName("Happy path for reporting Driver")
    public void Correct_input_test(){
        Long rideId = 1L;

        Ride mockRide = new Ride();

        Driver driver = new Driver();
        driver.setId(3L);
        driver.setFirstName("Vozac");
        driver.setLastName("Vozacevic");

        mockRide.setDriver(driver);

        Client mockClient = new Client();
        mockClient.setId(1L);

        ClientRide clientRide = new ClientRide(mockClient);
        clientRide.setClient(mockClient);

        Set<ClientRide> clientRides = new HashSet<>();
        clientRides.add(clientRide);

        mockRide.setClientsInfo(clientRides);

        Mockito.when(rideRepository.findDetailedById(rideId))
                .thenReturn(Optional.of(mockRide));

        Mockito.when(messageRepository.save(Mockito.any(Message.class)))
                .thenAnswer(i -> {
                    Message argument = i.getArgument(0);
                    argument.setId(1L);
                    return argument;
                });

        Message expectedMessage = new Message();
        expectedMessage.setSender(mockClient);
        expectedMessage.setText("REPORT: Driver: #" + driver.getId() + " " + driver.getFirstName() + " " + driver.getLastName() + " didn't follow the route for ride #" + mockRide.getId());

        Message returnedMessage = this.rideService.reportDriver(mockClient, rideId);
        Assertions.assertEquals(expectedMessage.getText(), returnedMessage.getText());
    }


}
