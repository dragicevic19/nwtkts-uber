package com.nwtkts.uber.service;

import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.Vehicle;

import java.util.List;

public interface RideService {
    Ride createRide(Ride ride, Vehicle vehicle);

    Ride changeRide(Long id);

    List<Ride> getRides();

    void deleteAllRides();
}
