package com.nwtkts.uber.service;

import com.nwtkts.uber.dto.RideRequest;
import com.nwtkts.uber.dto.RouteDTO;
import com.nwtkts.uber.model.*;

import java.util.List;

public interface RideService {
    Ride createRide(Ride ride, Vehicle vehicle, Driver driver);

    List<Ride> getRides();

    List<Ride> getDetailedRides();

    void deleteAllRides();

    Ride makeRideRequest(Client client, RideRequest rideRequest);

    Ride getRideForDriver(Long id);

    Ride endRide(Long id);

    Ride endFakeRide(Long id);

    Ride getDetailedRideForDriver(Long id);

    Route addRouteToFavorites(Client client, RouteDTO routeRequest);
}
