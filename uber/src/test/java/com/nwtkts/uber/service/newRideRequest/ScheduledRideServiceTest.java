package com.nwtkts.uber.service.newRideRequest;

import com.nwtkts.uber.model.*;
import com.nwtkts.uber.repository.DriverRepository;
import com.nwtkts.uber.repository.RideRepository;
import com.nwtkts.uber.service.ClientService;
import com.nwtkts.uber.service.FindDriverService;
import com.nwtkts.uber.service.impl.ScheduledRidesServiceImpl;
import jdk.jshell.execution.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ScheduledRideServiceTest {

    @Mock
    private FindDriverService findDriverService;
    @InjectMocks
    private ScheduledRidesServiceImpl scheduledRidesService;


    @Test
    @DisplayName("Should return unchanged ride when new scheduled ride is not in next 30 minutes")
    public void shouldReturnUnchangedRide() {
        Ride ride = makeRide();
        ride.setScheduledFor(LocalDateTime.now().plusMinutes(40));
        Ride returned = this.scheduledRidesService.processNewScheduledRide(ride);
        Assertions.assertEquals(ride.getRideStatus(), returned.getRideStatus());
        Assertions.assertNull(returned.getDriver());
    }

    @Test
    @DisplayName("Should cancel the ride when ride should start in 6 minutes, but driver is not found")
    public void shouldCancelTheRide() {
        Ride ride = makeRide();
        ride.setScheduledFor(LocalDateTime.now().plusMinutes(5));

        Mockito.when(findDriverService.searchDriver(Mockito.any(Ride.class)))
                .thenReturn(null);

        Ride returned = this.scheduledRidesService.processNewScheduledRide(ride);
        Assertions.assertEquals(RideStatus.CANCELED, returned.getRideStatus());
        Assertions.assertEquals("Can't find driver", returned.getCancellationReason());
    }

    @Test
    @DisplayName("Should return unchanged ride when ride should start in 15 minutes, but driver is not found")
    public void shouldReturnUnchangedRideWhenItsNotClose() {
        Ride ride = makeRide();
        ride.setScheduledFor(LocalDateTime.now().plusMinutes(15));

        Mockito.when(findDriverService.searchDriver(Mockito.any(Ride.class)))
                .thenReturn(null);

        Ride returned = this.scheduledRidesService.processNewScheduledRide(ride);
        Assertions.assertEquals(RideStatus.SCHEDULED, returned.getRideStatus());
        Assertions.assertNull(returned.getDriver());
    }

    @Test
    @DisplayName("Should find driver for ride when ride starts in next 30 minutes")
    public void shouldFindDriverForRide() {
        Ride ride = makeRide();
        ride.setScheduledFor(LocalDateTime.now().plusMinutes(15));

        Driver driver = new Driver();
        driver.setId(1L);
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        driver.setVehicle(vehicle);

        Mockito.when(findDriverService.searchDriver(Mockito.any(Ride.class)))
                .thenReturn(driver);

        Ride returned = this.scheduledRidesService.processNewScheduledRide(ride);
        Assertions.assertEquals(RideStatus.SCHEDULED, returned.getRideStatus());
        Assertions.assertEquals(driver.getId(), returned.getDriver().getId());
        Assertions.assertEquals(vehicle.getId(), returned.getVehicle().getId());
    }

    @Test
    @DisplayName("Should return status TO_PICKUP with driver for ride that starts in next 4 minutes")
    public void shouldFindDriverForRideAndSendCarToPickup() {
        Ride ride = makeRide();
        ride.setScheduledFor(LocalDateTime.now().plusMinutes(3));

        Driver driver = new Driver();
        driver.setId(1L);
        driver.setAvailable(true);
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        driver.setVehicle(vehicle);

        Mockito.when(findDriverService.searchDriver(Mockito.any(Ride.class)))
                .thenReturn(driver);

        Ride returned = this.scheduledRidesService.processNewScheduledRide(ride);
        Assertions.assertEquals(RideStatus.TO_PICKUP, returned.getRideStatus());
        Assertions.assertEquals(driver.getId(), returned.getDriver().getId());
        Assertions.assertEquals(vehicle.getId(), returned.getVehicle().getId());
        Assertions.assertEquals(false, returned.getDriver().getAvailable());
    }

    @Test
    @DisplayName("Should return status SCHEDULED with driver for ride that starts in next 4 minutes but driver is still not available")
    public void shouldFindDriverForRideAndScheduledStatusStill() {
        Ride ride = makeRide();
        ride.setScheduledFor(LocalDateTime.now().plusMinutes(3));

        Driver driver = new Driver();
        driver.setId(1L);
        driver.setAvailable(false);
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        driver.setVehicle(vehicle);

        Mockito.when(findDriverService.searchDriver(Mockito.any(Ride.class)))
                .thenReturn(driver);

        Ride returned = this.scheduledRidesService.processNewScheduledRide(ride);
        Assertions.assertEquals(RideStatus.SCHEDULED, returned.getRideStatus());
        Assertions.assertEquals(driver.getId(), returned.getDriver().getId());
        Assertions.assertEquals(vehicle.getId(), returned.getVehicle().getId());
        Assertions.assertEquals(false, returned.getDriver().getAvailable());
    }

    private Ride makeRide() {
        Ride newRide = new Ride();
        newRide.setId(1L);
        newRide.setRideStatus(RideStatus.SCHEDULED);
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
