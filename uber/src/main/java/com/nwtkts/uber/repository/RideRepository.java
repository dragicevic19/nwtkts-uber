package com.nwtkts.uber.repository;

import com.nwtkts.uber.model.Ride;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Ride, Long> {

    @EntityGraph(attributePaths = {"destinations", "clientsInfo"})
    Ride findDetailedById(Long id);

    Ride findSummaryById(Long id);
}
