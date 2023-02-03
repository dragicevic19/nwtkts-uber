package com.nwtkts.uber.repository;

import com.nwtkts.uber.model.ClientRide;
import com.nwtkts.uber.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientRideRepository extends JpaRepository<ClientRide, Long> {

    @Query(nativeQuery = true, value="SELECT * FROM client_ride WHERE ride_id=?1 AND client_id=?2")
    ClientRide findClientRideByRideId(Long rideId, Long clientId);

    @Query(nativeQuery = true, value="SELECT * FROM client_ride WHERE ride_id=?1")
    List<ClientRide> findClientsForRide(Long rideId);

}
