package com.nwtkts.uber.repository;

import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    @EntityGraph(attributePaths = {"activities"})
    Driver findDetailedByEmail(String email);

    Driver findSummaryByEmail(String email);

    List<Driver> findAllByActive(boolean active);

    Driver findByVehicle_Id(Long vehicleId);

    @EntityGraph(attributePaths = {"activities", "vehicle"})
    List<Driver> findAllDetailedByActiveAndAvailableAndBlocked(boolean active, boolean available, boolean blocked);

    @EntityGraph(attributePaths = {"activities", "vehicle"})
    List<Driver> findAllDetailedByAvailableAndBlocked(boolean available, boolean blocked);
}
