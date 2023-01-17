package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.dto.RideRequest;
import com.nwtkts.uber.dto.RouteDTO;
import com.nwtkts.uber.dto.UserProfile;
import com.nwtkts.uber.exception.NotFoundException;
import com.nwtkts.uber.model.*;
import com.nwtkts.uber.repository.*;
import com.nwtkts.uber.service.ClientService;
import com.nwtkts.uber.service.RequestRideService;
import com.nwtkts.uber.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class RideServiceImpl implements RideService {

    private final RequestRideService requestRideService;
    private final RideRepository rideRepository;
    private final VehicleRepository vehicleRepository;
    private final ClientRepository clientRepository;
    private final DriverRepository driverRepository;

    @Autowired
    public RideServiceImpl(RideRepository rideRepository, VehicleRepository vehicleRepository,
                           RequestRideService requestRideService, ClientRepository clientRepository,
                           DriverRepository driverRepository) {

        this.rideRepository = rideRepository;
        this.requestRideService = requestRideService;
        this.vehicleRepository = vehicleRepository;
        this.clientRepository = clientRepository;
        this.driverRepository = driverRepository;
    }

    @Override
    public Ride createRide(Ride ride, Vehicle vehicle, Driver driver) {
        Ride returnRide = this.rideRepository.save(ride);

        Vehicle storedVehicle = this.vehicleRepository.findById(vehicle.getId()).orElseThrow(
                () -> new NotFoundException("Vehicle does not exist"));
        Driver storedDriver = this.driverRepository.findById(driver.getId()).orElseThrow(
                () -> new NotFoundException("Driver does not exist!"));

        returnRide.setVehicle(storedVehicle);
        returnRide.setDriver(storedDriver);
        return this.rideRepository.save(returnRide);
    }

    @Override
    public Ride endRide(Long id) {
        Ride ride = this.rideRepository.findById(id).orElseThrow(() -> new NotFoundException("Ride does not exist!"));
        ride.setRideStatus(RideStatus.ENDED);
        this.rideRepository.save(ride);
        Driver driver = ride.getDriver();
        if (driver.getNextRideId() != null) {
            this.goForNextRide(driver);
        } else {
            driver.setAvailable(true);
            driver.setNextRideId(null);
        }
        this.driverRepository.save(driver);
        return ride;
    }

    private void goForNextRide(Driver driver) {
        Ride nextRide = this.rideRepository.findDetailedById(driver.getNextRideId()).orElseThrow(()
                -> new NotFoundException("Next ride doesn't exist!"));
        nextRide.setRideStatus(RideStatus.STARTED);
        nextRide.setStartTime(LocalDateTime.now());
        driver.setNextRideId(null);
        this.rideRepository.save(nextRide);
    }

    @Override
    public Ride endFakeRide(Long id) {
        Ride ride = this.rideRepository.findById(id).orElseThrow(() -> new NotFoundException("Ride does not exist!"));
        ride.setRideStatus(RideStatus.ENDED);
        return this.rideRepository.save(ride);
    }

    @Override
    public Ride getRideForDriver(Long driverId) {
        return this.rideRepository.findByRideStatusAndDriver_Id(RideStatus.STARTED, driverId);
    }

    @Override
    public Ride getDetailedRideForDriver(Long driverId) {
        return this.rideRepository.findDetailedByRideStatusAndDriver_Id(RideStatus.STARTED, driverId);
    }

    @Override
    public List<Ride> getRides() {
        return this.rideRepository.findAllByRideStatusOrRideStatus(RideStatus.STARTED, RideStatus.CRUISING);
    }

    @Override
    public List<Ride> getDetailedRides() {
        return this.rideRepository.findDetailedByRideStatusOrRideStatus(RideStatus.STARTED, RideStatus.CRUISING);
    }

    @Override
    public void deleteAllRides() {
        this.rideRepository.deleteAll();
    }

    @Override
    @Transactional
    public Ride makeRideRequest(Client client, RideRequest rideRequest) {
        return this.requestRideService.makeRideRequest(client, rideRequest);
    }

    @Override
    @Transactional
    public Route addRouteToFavorites(Client client, RouteDTO routeRequest) {
        Route newRoute = new Route(routeRequest);
        newRoute.setName("Route" + (client.getFavoriteRoutes().size() + 1));
        client.getFavoriteRoutes().add(newRoute);
        this.clientRepository.save(client);
        return newRoute;
    }
}
