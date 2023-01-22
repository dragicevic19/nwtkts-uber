package com.nwtkts.uber.repository;

import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.RideStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride, Long> {

    @EntityGraph(attributePaths = {"clientsInfo", "vehicle", "driver", "locationNames"})
    Optional<Ride> findDetailedById(Long id);

    Optional<Ride> findSummaryById(Long id);

    @EntityGraph(attributePaths = {"clientsInfo", "vehicle", "driver", "locationNames"})
    List<Ride> findAllDetailedByRideStatusIn(Collection<RideStatus> rideStatuses);
    List<Ride> findAllByRideStatusIsIn(Collection<RideStatus> rideStatuses);

    @EntityGraph(attributePaths = {"clientsInfo", "vehicle", "driver", "locationNames"})
    List<Ride> findAllDetailedByRideStatusAndDriver_Id(RideStatus rideStatus, Long driverId);

    Ride findByDriver_IdAndRideStatusIn(Long driverId, Collection<RideStatus> acceptableStatuses);
    @EntityGraph(attributePaths = {"clientsInfo", "vehicle", "driver", "locationNames"})
    Ride findDetailedByDriver_IdAndRideStatusIn(Long driverId, Collection<RideStatus> acceptableStatuses);

    @EntityGraph(attributePaths = {"clientsInfo", "vehicle", "driver", "locationNames"})
    List<Ride> findAllDetailedByDriver_IdAndRideStatusIn(Long driverId, Collection<RideStatus> acceptableStatuses);

//    @EntityGraph(attributePaths = {"destinations", "clientsInfo"})
    @Query(nativeQuery = true, value="SELECT * \n" +
            "FROM ride\n" +
            "INNER JOIN client_ride ON ride.id=client_ride.ride_id\n" +
            "WHERE ride.ride_status='ENDED' AND client_ride.client_id =?1")
    List<Ride> findAllEndedRidesOfClient(Long clientId);

    @Query(nativeQuery = true, value="SELECT * FROM ride WHERE ride_status='ENDED' AND driver_id=?1")
    List<Ride> findAllEndedRidesOfDriver(Long driverId);

    @Query(nativeQuery = true, value="SELECT * FROM ride WHERE ride_status='ENDED'")
    List<Ride> findAllEndedRides();


    @Query(nativeQuery = true, value="SELECT location_names FROM ride_location_names WHERE ride_id=?1 ORDER BY address_position ASC")
    List<String> findAllLocationNamesOfRide(Long rideId);

}
