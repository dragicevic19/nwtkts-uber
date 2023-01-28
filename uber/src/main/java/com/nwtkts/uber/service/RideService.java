package com.nwtkts.uber.service;

import com.nwtkts.uber.dto.*;
import com.nwtkts.uber.model.*;
import com.nwtkts.uber.model.ClientRide;
import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RideService {
    Ride createRide(Ride ride, Vehicle vehicle, Driver driver);

    List<Ride> getRides();

    List<Ride> getDetailedRides();

    void deleteAllRides();

    Ride makeRideRequest(Client client, RideRequest rideRequest);

    Ride getRideForDriverLocust(Long id);

    List<Ride> endRide(Long id);

    Ride endFakeRide(Long id);

    Ride getDetailedActiveRideForDriver(Long id);

    Route addRouteToFavorites(Client client, FavRouteDTO routeRequest);

    List<Ride> checkScheduledRides();

    Ride getRideById(Long rideId);

    Ride startRide(Ride ride);

    Ride getDetailedRideById(Long id);

    String generateNotificationForClientsScheduledRide(Ride ride);

    List<Ride> getActiveRidesForDriver(Long id);

    List<Ride> getSplitFareRequestsForClient(Client client);

    Ride acceptSplitFareReq(Client client, Long rideId);
    
    Page<Ride> getAllEndedRidesOfClient(Long userId, String userRole,Pageable page, String sort, String order);
    
    Ride findRideById(Long rideId);

    ClientRide findClientRide(Long rideId, Long clientId);

    List<ClientRide> findClientsForRide(Long rideId);

    void rateRide(User user, RideRatingDTO rideRatingDTO);

    Ride cancelRideDriver(Driver driver, RideCancelationDTO rideCancelationDTO);

    List<Ride> finishRideDriver(Driver driver, Long rideId);

    List<Ride> getActiveRidesForClient(Client client);

    Message reportDriver(Client client, Long rideId);
    
    ReportResponse getClientReport(User user, ReportDTO reportDTO);

}
