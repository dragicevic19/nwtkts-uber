package com.nwtkts.uber.repository;

import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.RideStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride, Long> {

    @EntityGraph(attributePaths = {"clientsInfo", "vehicle", "driver"})
    Optional<Ride> findDetailedById(Long id);

    Optional<Ride> findSummaryById(Long id);

    @EntityGraph(attributePaths = {"clientsInfo", "vehicle", "driver"})
    List<Ride> findAllDetailedByRideStatusOrRideStatus(RideStatus rideStatus, RideStatus rideStatus2);

    @EntityGraph(attributePaths = {"clientsInfo", "vehicle", "driver"})
    List<Ride> findAllDetailedByRideStatusAndDriver_Id(RideStatus rideStatus, Long driverId);

    Ride findByRideStatusOrRideStatusAndDriver_Id(RideStatus rideStatus1, RideStatus rideStatus2, Long id);

    Ride findByRideStatusOrRideStatusOrRideStatusAndDriver_Id(RideStatus rideStatus1, RideStatus rideStatus2, RideStatus rideStatus3, Long driverId);

    @EntityGraph(attributePaths = {"clientsInfo", "vehicle", "driver"})
    Ride findDetailedByRideStatusOrRideStatusOrRideStatusAndDriver_Id(RideStatus rideStatus1, RideStatus rideStatus2, RideStatus rideStatus3, Long driverId);

    List<Ride> findAllByRideStatusOrRideStatusOrRideStatusOrRideStatus(RideStatus rideStatus1, RideStatus rideStatus2, RideStatus rideStatus3, RideStatus rideStatus4);

    @EntityGraph(attributePaths = {"clientsInfo", "vehicle", "driver"})
    List<Ride> findDetailedByRideStatusOrRideStatusOrRideStatusOrRideStatus(RideStatus started, RideStatus cruising, RideStatus toPickup, RideStatus waitingForClient);

//    List<Ride> findAllByRideStatusIsInAndDriver_Id(RideStatus[] r1, Long driverId);
}
