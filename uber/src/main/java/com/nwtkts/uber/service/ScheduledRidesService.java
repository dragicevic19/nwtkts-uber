package com.nwtkts.uber.service;

import com.nwtkts.uber.model.Ride;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ScheduledRidesService {

    @Transactional
    Ride processNewScheduledRide(Ride newRide);

    @Transactional
    List<Ride> checkScheduledRides();

    void sendCar(Ride ride);

    void findDriver(Ride ride);
}
