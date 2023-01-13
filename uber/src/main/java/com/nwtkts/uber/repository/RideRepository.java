package com.nwtkts.uber.repository;

import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.RideStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride, Long> {

    @EntityGraph(attributePaths = {"destinations", "clientsInfo"})
    Optional<Ride> findDetailedById(Long id);

    Optional<Ride> findSummaryById(Long id);

    List<Ride> findAllByRideStatus(RideStatus rideStatus);

    Ride findByRideStatusAndDriver_Id(RideStatus rideStatus, Long driverId);

}
