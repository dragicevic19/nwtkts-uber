package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.RideStatus;
import com.nwtkts.uber.repository.DriverRepository;
import com.nwtkts.uber.repository.RideRepository;
import com.nwtkts.uber.service.ClientService;
import com.nwtkts.uber.service.RequestRideService;
import com.nwtkts.uber.service.ScheduledRidesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ScheduledRidesServiceImpl implements ScheduledRidesService {

    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private RequestRideService requestRideService;
    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private ClientService clientService;

    @Override
    @Transactional
    public List<Ride> checkScheduledRides() {
        List<Ride> ridesToNotify = new ArrayList<>();
        List<RideStatus> acceptableStatuses = new ArrayList<>(
                Arrays.asList(RideStatus.SCHEDULED, RideStatus.WAITING_FOR_PAYMENT));
        List<Ride> rides = this.rideRepository.findAllDetailedByRideStatusIn(acceptableStatuses);
        LocalDateTime now = LocalDateTime.now();

        for (Ride ride : rides) {
            this.checkIfPaymentIsDone(ride);
            if (ride.getRideStatus() == RideStatus.SCHEDULED && now.plusMinutes(30).isAfter(ride.getScheduledFor())) {
                this.scheduledIn30Minutes(ride, ridesToNotify);
            }
            this.rideRepository.save(ride);
        }
        return ridesToNotify;
    }

    private void scheduledIn30Minutes(Ride ride, List<Ride> ridesToNotify) {
        LocalDateTime now = LocalDateTime.now();

        if (ride.getDriver() == null) {
            if (now.plusMinutes(6).isAfter(ride.getScheduledFor())) {   // voznja je za 4 min a vozac jos uvek nije pronadjen
                cancelRide(ride, "Can't find driver");
            } else {
                findDriver(ride);
            }
        }
        if (ride.getDriver() != null) {
            if (now.plusMinutes(4).isAfter(ride.getScheduledFor()) && ride.getDriver().getAvailable()) {
                sendCar(ride);
            }
        }
        if (now.plusMinutes(20).isAfter(ride.getScheduledFor())) {
            ridesToNotify.add(ride);
        }
    }

    private void sendCar(Ride ride) {
        ride.getDriver().setAvailable(false);
        this.driverRepository.save(ride.getDriver());
        ride.setRideStatus(RideStatus.TO_PICKUP);
    }

    private void cancelRide(Ride ride, String cancellationReason) {
        ride.setRideStatus(RideStatus.CANCELED);
        ride.setCancellationReason(cancellationReason);
        this.clientService.refundToClients(ride);
    }

    private void findDriver(Ride ride) {
        // kako da vozac ne prima nove voznje oko ovog perioda? u searchDriver se proverava i to
        // driver.available? vozac ostaje dostupan za voznje koje moze da zavrsi pre pocetka ove scheduled

        Driver driver = this.requestRideService.searchDriver(ride);
        if (driver == null) return;
        ride.setDriver(driver);
        ride.setVehicle(driver.getVehicle());
        this.driverRepository.save(driver); // zbog nextRideId
    }

    private void checkIfPaymentIsDone(Ride ride) {
        LocalDateTime now = LocalDateTime.now();
        if (ride.getRideStatus() == RideStatus.WAITING_FOR_PAYMENT && ride.getScheduledFor() != null) {
            if (now.plusMinutes(20).isAfter(ride.getScheduledFor())) {  // mora da zavrsi placanje bar 20 minuta pre zakazane voznje da bi nasao vozaca
                cancelRide(ride, "Didn't complete payment for scheduled ride.");
            }
        }
    }
}
