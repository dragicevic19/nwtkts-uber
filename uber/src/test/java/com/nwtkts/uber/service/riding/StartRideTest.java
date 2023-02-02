package com.nwtkts.uber.service.riding;

import com.nwtkts.uber.exception.BadRequestException;
import com.nwtkts.uber.exception.NotFoundException;
import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.RideStatus;
import com.nwtkts.uber.repository.RideRepository;
import com.nwtkts.uber.service.impl.RideServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class StartRideTest {

    @Mock
    private RideRepository rideRepository;
    @InjectMocks
    private RideServiceImpl rideService;

    @Test
    @DisplayName("Should throw NotFoundException when ride doesn't exist in database.")
    public void NotFoundException_when_id_does_not_exists() {
        Long rideId = 1L;
        Mockito.when(rideRepository.findById(rideId))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> this.rideService.startRide(rideId));
    }

    @Test
    @DisplayName("Should throw BadRequest when RideStatus is not 'WAITING_FOR_CLIENT' before starting a ride")
    public void BadRequestException_when_ride_does_not_have_correct_status() {
        Long rideId = 1L;

        Ride mockRide = new Ride();
        mockRide.setRideStatus(RideStatus.STARTED);

        Mockito.when(rideRepository.findById(rideId))
                .thenReturn(Optional.of(mockRide));
        Assertions.assertThrows(BadRequestException.class, ()-> this.rideService.startRide(rideId));
    }

    @Test
    @DisplayName("Happy path for starting ride")
    public void Correct_input_test() {
        Long rideId = 1L;

        Ride mockRide = new Ride();
        mockRide.setRideStatus(RideStatus.WAITING_FOR_CLIENT);

        Mockito.when(rideRepository.findById(rideId))
                .thenReturn(Optional.of(mockRide));

        Mockito.when(rideRepository.save(Mockito.any(Ride.class))).thenAnswer(i -> {
            Ride argument = i.getArgument(0);
            argument.setId(1L);
            return argument;
        });

        this.rideService.startRide(rideId);

        Ride expectedRide = new Ride();
        expectedRide.setId(rideId);
        expectedRide.setRideStatus(RideStatus.STARTED);

        Assertions.assertEquals(expectedRide.getRideStatus(), mockRide.getRideStatus());
    }



}
