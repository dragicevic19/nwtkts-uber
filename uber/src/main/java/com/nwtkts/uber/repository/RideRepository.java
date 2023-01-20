package com.nwtkts.uber.repository;

import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.RideStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
