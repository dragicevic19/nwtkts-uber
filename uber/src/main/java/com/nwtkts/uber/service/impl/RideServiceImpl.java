package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.dto.RideRequest;
import com.nwtkts.uber.dto.RouteDTO;
import com.nwtkts.uber.exception.BadRequestException;
import com.nwtkts.uber.exception.NotFoundException;
import com.nwtkts.uber.model.*;
import com.nwtkts.uber.repository.*;
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
        if (nextRide.getScheduledFor() != null) return; // za zakazane unapred resavam u RideService -> checkScheduled

        driver.setAvailable(false);
        nextRide.setRideStatus(RideStatus.TO_PICKUP);
//        nextRide.setStartTime(LocalDateTime.now());
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
        return this.rideRepository.findByRideStatusOrRideStatusAndDriver_Id(RideStatus.STARTED, RideStatus.TO_PICKUP, driverId);
    }

    @Override
    public Ride getDetailedActiveRideForDriver(Long driverId) {
        return this.rideRepository.findDetailedByRideStatusOrRideStatusOrRideStatusAndDriver_Id(
                RideStatus.STARTED, RideStatus.WAITING_FOR_CLIENT, RideStatus.TO_PICKUP, driverId);
    }

    @Override
    public List<Ride> getRides() {
        return this.rideRepository.findAllByRideStatusOrRideStatusOrRideStatusOrRideStatus(RideStatus.STARTED, RideStatus.CRUISING, RideStatus.TO_PICKUP, RideStatus.WAITING_FOR_CLIENT);
//        return this.rideRepository.findAllByRideStatusOrRideStatus(RideStatus.STARTED, RideStatus.CRUISING);
    }

    @Override
    public List<Ride> getDetailedRides() {
        return this.rideRepository.findDetailedByRideStatusOrRideStatusOrRideStatusOrRideStatus(RideStatus.STARTED, RideStatus.CRUISING, RideStatus.TO_PICKUP, RideStatus.WAITING_FOR_CLIENT);
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
        List<Ride> ridesToLocust = new ArrayList<>();
        List<Ride> rides = this.rideRepository.findAllDetailedByRideStatusOrRideStatus(RideStatus.SCHEDULED, RideStatus.WAITING_FOR_PAYMENT);
        LocalDateTime now = LocalDateTime.now();

        for (Ride ride : rides) {
            boolean edited = false;
            if (ride.getRideStatus() == RideStatus.WAITING_FOR_PAYMENT && ride.getScheduledFor() != null) {
                if (now.plusMinutes(20).isAfter(ride.getScheduledFor())) {  // mora da zavrsi placanje bar 20 minuta pre zakazane voznje da bi nasao vozaca
                    ride.setRideStatus(RideStatus.CANCELED);
                    ride.setCancellationReason("Didn't complete payment for scheduled ride.");
                    edited = true;
                }
            } else if (ride.getRideStatus() == RideStatus.SCHEDULED) {
                if (now.isAfter(ride.getScheduledFor()) && ride.getDriver() == null) {
                    ride.setRideStatus(RideStatus.CANCELED);
                    ride.setCancellationReason("Can't find driver");
                    edited = true;
                } else if (now.plusMinutes(30).isAfter(ride.getScheduledFor())) {
                    if (ride.getDriver() == null) {
                        Driver driver = this.requestRideService.searchDriver(ride);
                        if (driver == null) continue;
                        // kako da vozac ne prima nove voznje oko ovog perioda? u searchDriver se proverava i to
                        // DRIVER.AVAILABLE? vozac ostaje dostupan za voznje koje moze da zavrsi pre pocetka ove scheduled
                        ride.setDriver(driver);
                        ride.setVehicle(driver.getVehicle());
                        edited = true;
                        this.driverRepository.save(driver); // zbog nextRideId ?
                    }
                    if (ride.getDriver() != null) {
                        if (now.plusMinutes(3).isAfter(ride.getScheduledFor()) && ride.getDriver().getAvailable()) {
                            // salji auto
                            ride.getDriver().setAvailable(false);
                            ride.getDriver().setNextRideId(null);
                            this.driverRepository.save(ride.getDriver());
                            ride.setRideStatus(RideStatus.TO_PICKUP);
                            edited = true;

                        }
                        if (now.plusMinutes(20).isAfter(ride.getScheduledFor())) {
                            ridesToLocust.add(ride);
                        }
                    }
                }
            }
            if (edited) this.rideRepository.save(ride);
        }
        return ridesToLocust;
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
        if (ride.getRideStatus() != RideStatus.SCHEDULED) return null;
        Long minutes = ChronoUnit.MINUTES.between(ride.getScheduledFor(), LocalDateTime.now());

        return "You have scheduled ride in " + minutes;

    }
}
