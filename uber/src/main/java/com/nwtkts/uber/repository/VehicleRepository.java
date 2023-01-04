package com.nwtkts.uber.repository;

import com.nwtkts.uber.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findVehicleByLicensePlateNumber(String licencePlateNumber);

    boolean existsByLicensePlateNumber(String licensePLateNUmber);
}
