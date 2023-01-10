package com.nwtkts.uber.service;

import com.nwtkts.uber.dto.FakeRideDTO;
import com.nwtkts.uber.model.Vehicle;

public interface VehicleService {
    Vehicle createVehicle(Vehicle vehicle);

    void deleteAllVehicles();

    Vehicle updateVehicleLocation(Long id, double latitude, double longitude);

    Vehicle getVehicleForDriverFake(FakeRideDTO rideDTO);

    Vehicle getVehicleForDriver(Long driverId);
}
