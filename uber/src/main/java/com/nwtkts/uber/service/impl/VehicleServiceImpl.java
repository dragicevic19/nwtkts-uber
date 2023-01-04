package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.exception.BadRequestException;
import com.nwtkts.uber.exception.NotFoundException;
import com.nwtkts.uber.model.Location;
import com.nwtkts.uber.model.Vehicle;
import com.nwtkts.uber.repository.VehicleRepository;
import com.nwtkts.uber.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
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
}
