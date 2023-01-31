package com.nwtkts.uber.service.newRideRequest;

import com.nwtkts.uber.dto.RideRequest;
import com.nwtkts.uber.dto.RouteDTO;
import com.nwtkts.uber.dto.UserProfile;
import com.nwtkts.uber.exception.BadRequestException;
import com.nwtkts.uber.exception.NotFoundException;
import com.nwtkts.uber.model.*;
import com.nwtkts.uber.repository.ClientRepository;
import com.nwtkts.uber.repository.DriverRepository;
import com.nwtkts.uber.repository.RideRepository;
import com.nwtkts.uber.repository.VehicleTypeRepository;
import com.nwtkts.uber.service.impl.ClientServiceImpl;
import com.nwtkts.uber.service.impl.FindDriverServiceImpl;
import com.nwtkts.uber.service.impl.RequestRideServiceImpl;
import com.nwtkts.uber.util.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class RequestRideServiceTest {

    private Client client;
    private RideRequest rideRequest;

    @Mock
    private VehicleTypeRepository vehicleTypeRepository;
    @Mock
    private ClientServiceImpl clientService;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private RideRepository rideRepository;
    @Mock
    private FindDriverServiceImpl findDriverService;
    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private RequestRideServiceImpl requestRideService;


    @BeforeEach
    public void setUp() {
        client = Utils.makeClient();
        rideRequest = Utils.makeRideRequest(false, false);
    }

    @Test
    @DisplayName("Should throw NotFoundException when vehicleType doesn't exist")
    public void shouldThrowNotFoundException() {
        Mockito.when(vehicleTypeRepository.findById(rideRequest.getVehicleType().getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> requestRideService.makeRideRequest(client, rideRequest));
    }

    @Test
    @DisplayName("Should throw NotFoundException when client sent in addedFriends doesn't exist")
    public void shouldThrowNotFoundExceptionWhenClientInRideDoesNotExist() {
        Mockito.when(vehicleTypeRepository.findById(rideRequest.getVehicleType().getId()))
                .thenReturn(Optional.of(new VehicleType(1L, "SEDAN", 1.)));

        Mockito.when(clientService.makePayment(Mockito.any(Client.class), Mockito.anyDouble()))
                .thenReturn(client);

        rideRequest.setAddedFriends(new ArrayList<>(List.of(new UserProfile(2L))));
        Mockito.when(clientRepository.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> requestRideService.makeRideRequest(client, rideRequest));
    }

    @Test
    @DisplayName("Should throw BadRequest when client sent in addedFriends is blocked")
    public void shouldThrowBadRequestWhenClientSentInAddedIsBlocked() {
        Mockito.when(vehicleTypeRepository.findById(rideRequest.getVehicleType().getId()))
                .thenReturn(Optional.of(new VehicleType(1L, "SEDAN", 1.)));

        Mockito.when(clientService.makePayment(Mockito.any(Client.class), Mockito.anyDouble()))
                .thenReturn(client);

        rideRequest.setAddedFriends(new ArrayList<>(List.of(new UserProfile(2L))));
        Client blockedClient = new Client();
        blockedClient.setBlocked(true);

        Mockito.when(clientRepository.findById(2L)).thenReturn(Optional.of(blockedClient));
        Assertions.assertThrows(BadRequestException.class, () -> requestRideService.makeRideRequest(client, rideRequest));
    }

    @Test
    @DisplayName("Should return SCHEDULED ride when ride request is scheduled")
    public void shouldReturnScheduledRide() {
        VehicleType vehicleType = new VehicleType(1L, "SEDAN", 1.);
        rideRequest.setScheduled("15:20");
        Mockito.when(vehicleTypeRepository.findById(rideRequest.getVehicleType().getId()))
                .thenReturn(Optional.of(vehicleType));

        client.setTokens(client.getTokens() - 3.6);
        Mockito.when(clientService.makePayment(Mockito.isA(Client.class), Mockito.anyDouble()))
                .thenReturn(client);

        Mockito.when(rideRepository.save(Mockito.any(Ride.class))).thenAnswer(i -> {
            Ride argument = i.getArgument(0);
            argument.setId(1L);
            return argument;
        });

        Ride expectedRide = new Ride(rideRequest, 1.3, vehicleType);
        expectedRide.setRideStatus(RideStatus.SCHEDULED);

        Ride actualRide = this.requestRideService.makeRideRequest(client, rideRequest);

        Assertions.assertEquals(expectedRide.getRideStatus(), actualRide.getRideStatus());
        Assertions.assertEquals(1, actualRide.getClientsInfo().size());
    }

    @Test
    @DisplayName("Should return CANCELED ride when driver is not founded")
    public void shouldReturnCanceled() {
        VehicleType vehicleType = new VehicleType(1L, "SEDAN", 1.);
        Mockito.when(vehicleTypeRepository.findById(rideRequest.getVehicleType().getId()))
                .thenReturn(Optional.of(vehicleType));

        client.setTokens(client.getTokens() - 3.6);
        Mockito.when(clientService.makePayment(Mockito.isA(Client.class), Mockito.anyDouble()))
                .thenReturn(client);

        Mockito.when(rideRepository.save(Mockito.any(Ride.class))).thenAnswer(i -> {
            Ride argument = i.getArgument(0);
            argument.setId(1L);
            return argument;
        });

        Mockito.when(findDriverService.searchDriver(Mockito.any(Ride.class)))
                .thenReturn(null);

        Mockito.when(clientService.refundForCanceledRide(Mockito.any(Client.class), Mockito.anyDouble()))
                .thenReturn(true);

        Ride expectedRide = new Ride(rideRequest, 1.3, vehicleType);
        expectedRide.setRideStatus(RideStatus.CANCELED);
        expectedRide.setCancellationReason("Can't find driver");

        Ride actualRide = this.requestRideService.makeRideRequest(client, rideRequest);

        Assertions.assertEquals(expectedRide.getRideStatus(), actualRide.getRideStatus());
        Assertions.assertEquals(expectedRide.getCancellationReason(), actualRide.getCancellationReason());
    }


    @Test
    @DisplayName("Should return TO_PICKUP ride when available driver is founded")
    public void shouldReturnToPickupWhenDriverIsFounded() {
        VehicleType vehicleType = new VehicleType(1L, "SEDAN", 1.);
        Mockito.when(vehicleTypeRepository.findById(rideRequest.getVehicleType().getId()))
                .thenReturn(Optional.of(vehicleType));

        client.setTokens(client.getTokens() - 3.6);
        Mockito.when(clientService.makePayment(Mockito.isA(Client.class), Mockito.anyDouble()))
                .thenReturn(client);

        Mockito.when(rideRepository.save(Mockito.any(Ride.class))).thenAnswer(i -> {
            Ride argument = i.getArgument(0);
            argument.setId(1L);
            return argument;
        });

        Driver availableDriver = new Driver();
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        availableDriver.setId(3L);
        availableDriver.setVehicle(vehicle);
        availableDriver.setAvailable(true);
        availableDriver.setActive(true);
        availableDriver.setNextRideId(null);

        Mockito.when(findDriverService.searchDriver(Mockito.any(Ride.class)))
                .thenReturn(availableDriver);

        Mockito.when(driverRepository.save(Mockito.any(Driver.class)))
                .thenReturn(new Driver());

        Ride expectedRide = new Ride(rideRequest, 1.3, vehicleType);
        expectedRide.setRideStatus(RideStatus.TO_PICKUP);

        Ride actualRide = this.requestRideService.makeRideRequest(client, rideRequest);

        Assertions.assertEquals(expectedRide.getRideStatus(), actualRide.getRideStatus());
        Assertions.assertEquals(3L, actualRide.getDriver().getId());
        Assertions.assertEquals(1L, actualRide.getVehicle().getId());
        Assertions.assertFalse(actualRide.getDriver().getAvailable());
    }


    @Test
    @DisplayName("Should return WAITING_FOR_DRIVER_TO_FINISH ride when active driver is available only for next ride")
    public void shouldReturnWaitingForDriverToFinishWhenNotAvailableDriverIsFounded() {
        VehicleType vehicleType = new VehicleType(1L, "SEDAN", 1.);
        Mockito.when(vehicleTypeRepository.findById(rideRequest.getVehicleType().getId()))
                .thenReturn(Optional.of(vehicleType));

        client.setTokens(client.getTokens() - 3.6);
        Mockito.when(clientService.makePayment(Mockito.isA(Client.class), Mockito.anyDouble()))
                .thenReturn(client);

        Mockito.when(rideRepository.save(Mockito.any(Ride.class))).thenAnswer(i -> {
            Ride argument = i.getArgument(0);
            argument.setId(1L);
            return argument;
        });

        Driver foundedDriver = new Driver();
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        foundedDriver.setId(3L);
        foundedDriver.setVehicle(vehicle);
        foundedDriver.setAvailable(false);
        foundedDriver.setActive(true);
        foundedDriver.setNextRideId(1L);
        Mockito.when(findDriverService.searchDriver(Mockito.any(Ride.class)))
                .thenReturn(foundedDriver);

        Mockito.when(driverRepository.save(Mockito.any(Driver.class)))
                .thenReturn(new Driver());

        Ride actualRide = this.requestRideService.makeRideRequest(client, rideRequest);

        Assertions.assertEquals(RideStatus.WAITING_FOR_DRIVER_TO_FINISH, actualRide.getRideStatus());
        Assertions.assertEquals(foundedDriver.getId(), actualRide.getDriver().getId());
        Assertions.assertEquals(vehicle.getId(), actualRide.getVehicle().getId());
        Assertions.assertFalse(actualRide.getDriver().getAvailable());
        Assertions.assertEquals(foundedDriver.getNextRideId(), actualRide.getDriver().getNextRideId());
    }


    @Test
    @DisplayName("Should return WAITING_FOR_PAYMENT ride status when there is more clients to pay")
    public void shouldReturnWaitingForPayment() {
        VehicleType vehicleType = new VehicleType(1L, "SEDAN", 1.);
        Mockito.when(vehicleTypeRepository.findById(rideRequest.getVehicleType().getId()))
                .thenReturn(Optional.of(vehicleType));

        client.setTokens(client.getTokens() - 3.6);
        Mockito.when(clientService.makePayment(Mockito.isA(Client.class), Mockito.anyDouble()))
                .thenReturn(client);

        Mockito.when(rideRepository.save(Mockito.any(Ride.class))).thenAnswer(i -> {
            Ride argument = i.getArgument(0);
            argument.setId(1L);
            return argument;
        });

        Client friendInRide = new Client();
        friendInRide.setBlocked(false);

        rideRequest.setAddedFriends(new ArrayList<>(List.of(new UserProfile(2L))));
        Mockito.when(clientRepository.findById(2L)).thenReturn(Optional.of(friendInRide));

        Ride actualRide = this.requestRideService.makeRideRequest(client, rideRequest);

        Assertions.assertEquals(RideStatus.WAITING_FOR_PAYMENT, actualRide.getRideStatus());
        Assertions.assertNull(actualRide.getDriver());
        Assertions.assertNull(actualRide.getVehicle());
    }


}
