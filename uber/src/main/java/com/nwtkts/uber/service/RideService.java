package com.nwtkts.uber.service;

import com.nwtkts.uber.dto.RideRequest;
import com.nwtkts.uber.dto.RouteDTO;
import com.nwtkts.uber.model.*;
import com.nwtkts.uber.model.ClientRide;
import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

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

    Ride acceptSplitFareReq(Client client, Long rideId);
    
    Page<Ride> getAllEndedRidesOfClient(Long userId, String userRole,Pageable page, String sort, String order);
    
    Optional<Ride> findRideById(Long rideId);

    ClientRide findClientRide(Long rideId, Long clientId);

}
