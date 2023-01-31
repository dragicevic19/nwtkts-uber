package com.nwtkts.uber.service.newRideRequest;

import com.nwtkts.uber.dto.RideRequest;
import com.nwtkts.uber.exception.BadRequestException;
import com.nwtkts.uber.exception.NotFoundException;
import com.nwtkts.uber.model.*;
import com.nwtkts.uber.repository.RideRepository;
import com.nwtkts.uber.service.RequestRideService;
import com.nwtkts.uber.service.impl.RideServiceImpl;
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

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RideServiceTest {

    private Client client;
    private RideRequest rideRequest;
    private Ride ride;

    @Mock
    private RequestRideService requestRideService;
    @Mock
    private RideRepository rideRepository;

    @InjectMocks
    private RideServiceImpl rideService;



    @BeforeEach
    public void setUp() {
        client = Utils.makeClient();
        rideRequest = Utils.makeRideRequest(false, false);
        ride = Utils.makeRideForClient(client, rideRequest);
    }

    @Test
    @DisplayName("Should throw BadRequestException when client has already started ride")
    public void shouldThrowBadRequest() {
        Mockito.when(rideRepository.findAllDetailedByRideStatusIn(List.of(RideStatus.STARTED)))
                .thenReturn(List.of(ride));

        Assertions.assertThrows(BadRequestException.class, () -> this.rideService.makeRideRequest(client, rideRequest));
    }

    @Test
    @DisplayName("Should throw NotFoundException when ride with given id doesn't exist")
    public void shouldThrowNotFoundException() {
        Mockito.when(rideRepository.findAllDetailedByRideStatusIn(List.of(RideStatus.STARTED)))
                .thenReturn(List.of());

        Mockito.when(requestRideService.makeRideRequest(client, rideRequest))
                .thenReturn(ride);

        Mockito.when(rideRepository.findDetailedById(ride.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> this.rideService.makeRideRequest(client, rideRequest));
    }

    @Test
    @DisplayName("Should return ride")
    public void shouldReturnRide() {
        Mockito.when(rideRepository.findAllDetailedByRideStatusIn(List.of(RideStatus.STARTED)))
                .thenReturn(List.of());

        Mockito.when(requestRideService.makeRideRequest(client, rideRequest))
                .thenReturn(ride);

        Mockito.when(rideRepository.findDetailedById(ride.getId()))
                .thenReturn(Optional.of(ride));

        Ride returnedRide = this.rideService.makeRideRequest(client, rideRequest);

        Assertions.assertEquals(ride.getId(), returnedRide.getId());
    }
}
