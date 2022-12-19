package com.nwtkts.uber.repository;

import com.nwtkts.uber.model.Driver;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    @EntityGraph(attributePaths = {"activities"})
    Driver findDetailedByEmail(String email);

    Driver findSummaryByEmail(String email);
}