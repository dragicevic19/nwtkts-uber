package com.nwtkts.uber.repository;

import com.nwtkts.uber.model.Route;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {

    @EntityGraph(attributePaths = {"coordinates"})
    Route findDetailedById(Long id);

    Optional<Route> findSummaryById(Long id);

}
