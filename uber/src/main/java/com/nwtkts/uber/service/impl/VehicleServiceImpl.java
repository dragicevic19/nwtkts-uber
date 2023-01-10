package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.dto.FakeRideDTO;
import com.nwtkts.uber.exception.BadRequestException;
import com.nwtkts.uber.exception.NotFoundException;
import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.Location;
import com.nwtkts.uber.model.Vehicle;
import com.nwtkts.uber.repository.DriverRepository;
import com.nwtkts.uber.repository.VehicleRepository;
import com.nwtkts.uber.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository, DriverRepository driverRepository) {
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
    }

    @Override
    public Vehicle createVehicle(Vehicle vehicle) {
        if (this.vehicleRepository.existsByLicensePlateNumber(vehicle.getLicensePlateNumber())) {
            throw new BadRequestException("Vehicle already exists!");
        }
        return this.vehicleRepository.save(vehicle);
    }

    @Override
    public void deleteAllVehicles() {
        this.vehicleRepository.deleteAll();
    }

    @Override
    public Vehicle updateVehicleLocation(Long id, double latitude, double longitude) {
        Vehicle vehicle = this.vehicleRepository.findById(id).orElseThrow(() -> new NotFoundException("Vehicle does not exist!"));
        vehicle.setCurrentLocation(new Location(latitude, longitude));
        return this.vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle getVehicleForDriverFake(FakeRideDTO rideDTO) {
        Driver driver = this.driverRepository.findById(rideDTO.getDriverId()).orElseThrow(() -> new NotFoundException("Driver does not exist!"));
        Vehicle vehicle = driver.getVehicle();
        vehicle.setCurrentLocation(new Location(rideDTO.getVehicleLatitude(), rideDTO.getVehicleLongitude()));
        return this.vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle getVehicleForDriver(Long driverId) {
        Driver driver = this.driverRepository.findById(driverId).orElseThrow(() -> new NotFoundException("Driver does not exist!"));
        return driver.getVehicle();
    }
}
