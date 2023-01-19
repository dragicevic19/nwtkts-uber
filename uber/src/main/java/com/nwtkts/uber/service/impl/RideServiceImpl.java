package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.dto.RideRequest;
import com.nwtkts.uber.dto.RouteDTO;
import com.nwtkts.uber.exception.BadRequestException;
import com.nwtkts.uber.exception.NotFoundException;
import com.nwtkts.uber.model.*;
import com.nwtkts.uber.repository.*;
import com.nwtkts.uber.service.RequestRideService;
import com.nwtkts.uber.service.RideService;
import com.nwtkts.uber.service.ScheduledRidesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RideServiceImpl implements RideService {

    private final RequestRideService requestRideService;
    private final ScheduledRidesService scheduledRidesService;
    private final RideRepository rideRepository;
    private final VehicleRepository vehicleRepository;
    private final ClientRepository clientRepository;
    private final DriverRepository driverRepository;

    @Autowired
    public RideServiceImpl(RideRepository rideRepository, VehicleRepository vehicleRepository,
                           RequestRideService requestRideService, ClientRepository clientRepository,
                           DriverRepository driverRepository, ScheduledRidesService scheduledRidesService) {

        this.rideRepository = rideRepository;
        this.requestRideService = requestRideService;
        this.vehicleRepository = vehicleRepository;
        this.clientRepository = clientRepository;
        this.driverRepository = driverRepository;
        this.scheduledRidesService = scheduledRidesService;
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
        Ride ride = this.rideRepository.findDetailedById(id).orElseThrow(() -> new NotFoundException("Ride does not exist!"));

        if (ride.getRideStatus() == RideStatus.TO_PICKUP) {
            ride.setRideStatus(RideStatus.WAITING_FOR_CLIENT);
            return this.rideRepository.save(ride);
        } else if (ride.getRideStatus() == RideStatus.STARTED) {
            ride.setRideStatus(RideStatus.ENDED);
            this.rideRepository.save(ride);

            Driver driver = ride.getDriver();
            driver.setAvailable(true);
            if (driver.getNextRideId() != null) {
                this.goForNextRide(driver);
            } else {
                driver.setNextRideId(null);
            }
            this.driverRepository.save(driver);
            return ride;
        } else {
            throw new BadRequestException("END RIDE BAD REQUEST");
        }
    }

    private void goForNextRide(Driver driver) {
        Ride nextRide = this.rideRepository.findDetailedById(driver.getNextRideId()).orElseThrow(()
                -> new NotFoundException("Next ride doesn't exist!"));
        if (nextRide.getScheduledFor() != null) {
            return; // za zakazane unapred resavam u RideService -> checkScheduled
        }

        driver.setAvailable(false);
        nextRide.setRideStatus(RideStatus.TO_PICKUP);
        driver.setNextRideId(null);
        this.rideRepository.save(nextRide);
    }

    @Override
    public Ride endFakeRide(Long id) {
        Ride ride = this.rideRepository.findById(id).orElseThrow(() -> new NotFoundException("Ride does not exist!"));
        this.rideRepository.delete(ride);
        return ride;
    }

    @Override
    public Ride getRideForDriverLocust(Long driverId) {
        List<RideStatus> acceptableStatuses = new ArrayList<>(Arrays.asList(RideStatus.STARTED, RideStatus.TO_PICKUP));
        return this.rideRepository.findByDriver_IdAndRideStatusIn(driverId, acceptableStatuses);
    }

    @Override
    public Ride getDetailedActiveRideForDriver(Long driverId) {
        List<RideStatus> acceptableStatuses = new ArrayList<>(Arrays.asList(RideStatus.STARTED, RideStatus.WAITING_FOR_CLIENT, RideStatus.TO_PICKUP));
        return this.rideRepository.findDetailedByDriver_IdAndRideStatusIn(driverId, acceptableStatuses);
    }

    @Override
    public List<Ride> getRides() {
        List<RideStatus> acceptableStatuses = new ArrayList<>(
                Arrays.asList(RideStatus.CRUISING, RideStatus.TO_PICKUP, RideStatus.WAITING_FOR_CLIENT, RideStatus.STARTED));
        return this.rideRepository.findAllByRideStatusIsIn(acceptableStatuses);
    }

    @Override
    public List<Ride> getDetailedRides() {
        List<RideStatus> acceptableStatuses = new ArrayList<>(
                Arrays.asList(RideStatus.CRUISING, RideStatus.TO_PICKUP, RideStatus.WAITING_FOR_CLIENT, RideStatus.STARTED));
        return this.rideRepository.findAllDetailedByRideStatusIn(acceptableStatuses);
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

    @Override
    @Transactional
    public List<Ride> checkScheduledRides() {
        return this.scheduledRidesService.checkScheduledRides();
    }

    @Override
    public Ride getRideById(Long rideId) {
        return this.rideRepository.findById(rideId).orElseThrow(() -> new NotFoundException("Ride doesn't exist!"));
    }

    @Override
    public Ride startRide(Ride ride) {
        ride.setRideStatus(RideStatus.STARTED);
        ride.setStartTime(LocalDateTime.now());
        return this.rideRepository.save(ride);
    }

    @Override
    public Ride getDetailedRideById(Long id) {
        return this.rideRepository.findDetailedById(id).orElseThrow(() -> new NotFoundException("Ride doesn't exist!"));
    }

    @Override
    public String generateNotificationForClientsScheduledRide(Ride ride) {
        if (ride.getRideStatus() == RideStatus.CANCELED) return "System couldn't find driver for you. Ride is canceled!";
        if (ride.getRideStatus() != RideStatus.SCHEDULED) return null;
        Long minutes = Math.abs(ChronoUnit.MINUTES.between(ride.getScheduledFor(), LocalDateTime.now()));

        return "You have scheduled ride in " + minutes + " minutes";
    }

    @Override
    public List<Ride> getRidesForDriver(Long id) {
        List<RideStatus> acceptableStatuses = new ArrayList<>(
                Arrays.asList(RideStatus.TO_PICKUP, RideStatus.WAITING_FOR_CLIENT, RideStatus.STARTED, RideStatus.SCHEDULED, RideStatus.WAITING_FOR_DRIVER_TO_FINISH));
        return this.rideRepository.findAllDetailedByDriver_IdAndRideStatusIn(id, acceptableStatuses);
    }
}
