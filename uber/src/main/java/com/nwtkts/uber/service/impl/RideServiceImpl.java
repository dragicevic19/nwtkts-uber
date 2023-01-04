package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.exception.NotFoundException;
import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.RideStatus;
import com.nwtkts.uber.model.Vehicle;
import com.nwtkts.uber.repository.RideRepository;
import com.nwtkts.uber.repository.VehicleRepository;
import com.nwtkts.uber.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public RideServiceImpl(RideRepository rideRepository, VehicleRepository vehicleRepository) {
        this.rideRepository = rideRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Ride createRide(Ride ride, Vehicle vehicle) {
        Ride returnRide = this.rideRepository.save(ride);
        Vehicle storedVehicle = this.vehicleRepository.findById(vehicle.getId()).orElseThrow(
                () -> new NotFoundException("Vehicle does not exist"));
        returnRide.setVehicle(storedVehicle);
        return this.rideRepository.save(ride);
    }

    @Override
    public Ride changeRide(Long id) {
        Ride ride = this.rideRepository.findById(id).orElseThrow(() -> new NotFoundException("Ride does not exist!"));
        ride.setRideStatus(RideStatus.ENDED);
        return this.rideRepository.save(ride);
    }

    @Override
    public List<Ride> getRides() {
        return this.rideRepository.findAllByRideStatus(RideStatus.STARTED);
    }

    @Override
    public void deleteAllRides() {
        this.rideRepository.deleteAll();
    }
}
