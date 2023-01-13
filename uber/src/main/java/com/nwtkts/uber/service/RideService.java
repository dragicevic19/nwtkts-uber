package com.nwtkts.uber.service;

import com.nwtkts.uber.dto.RideRequest;
import com.nwtkts.uber.model.Client;
import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.Vehicle;

import java.util.List;

public interface RideService {
    Ride createRide(Ride ride, Vehicle vehicle, Driver driver);

    Ride changeRide(Long id);

    List<Ride> getRides();

    void deleteAllRides();

    Ride makeRideRequest(Client client, RideRequest rideRequest);
}
