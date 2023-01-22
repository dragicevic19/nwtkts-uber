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

    Ride getRideForDriverLocust(Long id);

    Ride endRide(Long id);

    Ride endFakeRide(Long id);

    Ride getDetailedActiveRideForDriver(Long id);

    Route addRouteToFavorites(Client client, RouteDTO routeRequest);

    List<Ride> checkScheduledRides();

    Ride getRideById(Long rideId);

    Ride startRide(Ride ride);

    Ride getDetailedRideById(Long id);

    String generateNotificationForClientsScheduledRide(Ride ride);

    List<Ride> getRidesForDriver(Long id);

    List<Ride> getSplitFareRequestsForClient(Client client);

    void acceptSplitFareReq(Client client, Long rideId);
}
