package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.dto.RideRequest;
import com.nwtkts.uber.dto.RouteDTO;
import com.nwtkts.uber.exception.BadRequestException;
import com.nwtkts.uber.exception.NotFoundException;
import com.nwtkts.uber.model.*;
import com.nwtkts.uber.repository.*;
import com.nwtkts.uber.service.ClientService;
import com.nwtkts.uber.service.RequestRideService;
import com.nwtkts.uber.repository.ClientRideRepository;
import com.nwtkts.uber.repository.DriverRepository;
import com.nwtkts.uber.repository.RideRepository;
import com.nwtkts.uber.repository.VehicleRepository;
import com.nwtkts.uber.service.RideService;
import com.nwtkts.uber.service.ScheduledRidesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RideServiceImpl implements RideService {

    private final RequestRideService requestRideService;
    private final ClientService clientService;
    private final ScheduledRidesService scheduledRidesService;
    private final RideRepository rideRepository;
    private final VehicleRepository vehicleRepository;
    private final ClientRepository clientRepository;
    private final DriverRepository driverRepository;
    private final ClientRideRepository clientRideRepository;

    @Autowired
    public RideServiceImpl(RideRepository rideRepository, VehicleRepository vehicleRepository,
                           RequestRideService requestRideService, ClientRepository clientRepository,
                           DriverRepository driverRepository, ScheduledRidesService scheduledRidesService,
                           ClientService clientService,
                           ClientRideRepository clientRideRepository) {

        this.rideRepository = rideRepository;
        this.requestRideService = requestRideService;
        this.vehicleRepository = vehicleRepository;
        this.clientRepository = clientRepository;
        this.driverRepository = driverRepository;
        this.scheduledRidesService = scheduledRidesService;
        this.clientService = clientService;
        this.clientRideRepository = clientRideRepository;
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
        Ride ride = this.requestRideService.makeRideRequest(client, rideRequest);
        if (ride.getRideStatus() == RideStatus.SCHEDULED) this.scheduledRidesService.checkScheduledRides();
        return ride;
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
        if (ride.getScheduledFor().plusMinutes(16).isAfter(LocalDateTime.now())) {
            long minutes = Math.abs(ChronoUnit.MINUTES.between(ride.getScheduledFor(), LocalDateTime.now()));
            if (minutes % 5 == 0) {
                if (ride.getRideStatus() == RideStatus.CANCELED) return ride.getCancellationReason() + ". Ride is canceled!";
                if (ride.getRideStatus() != RideStatus.SCHEDULED && ride.getRideStatus() != RideStatus.TO_PICKUP) return null;
                return "You have scheduled ride in " + minutes + " minutes";
            }
        }
        return null;
    }

    @Override
    public List<Ride> getRidesForDriver(Long id) {
        List<RideStatus> acceptableStatuses = new ArrayList<>(
                Arrays.asList(RideStatus.TO_PICKUP, RideStatus.WAITING_FOR_CLIENT, RideStatus.STARTED, RideStatus.SCHEDULED, RideStatus.WAITING_FOR_DRIVER_TO_FINISH));
        return this.rideRepository.findAllDetailedByDriver_IdAndRideStatusIn(id, acceptableStatuses);
    }

    @Override
    public List<Ride> getSplitFareRequestsForClient(Client client) {
        List<Ride> clientsRequests = new ArrayList<>();
        List<Ride> rides = this.rideRepository.findAllDetailedByRideStatusIn(Arrays.asList(RideStatus.WAITING_FOR_PAYMENT));
        for (Ride ride: rides) {
            for(ClientRide clientRide : ride.getClientsInfo()) {
                if (clientRide.getClient().getId() == client.getId()) {
                    if (!clientRide.isClientPaid()) {
                        clientsRequests.add(ride);
                        break;
                    }
                }
            }
        }
        return clientsRequests;
    }

    @Override
    public Ride acceptSplitFareReq(Client client, Long rideId) {
        Ride ride = this.rideRepository.findDetailedById(rideId).orElseThrow(() -> new NotFoundException("Ride doesn't exist"));
        for (ClientRide clientRide: ride.getClientsInfo()) {
            if (clientRide.getClient().getId() == client.getId()) {

                if (clientRide.isClientPaid()) throw new BadRequestException("Client already paid for this ride");

                this.clientService.makePayment(client, ride.getPrice() / ride.getClientsInfo().size());
                clientRide.setClientPaid(true);

                return this.requestRideService.afterClientPays(client, ride);
            }
        }
        throw new NotFoundException("Can't find ride with this ID for client");
    }
    
    public Page<Ride> getAllEndedRidesOfClient(Long userId, String userRole,Pageable page, String sort, String order) {
//        desc, asc
//        startTime, calculatedDuration, price

        List<Ride> queryList = null;
        if (userRole.equals("ROLE_CLIENT"))
            queryList = rideRepository.findAllEndedRidesOfClient(userId);
        else if (userRole.equals("ROLE_DRIVER"))
            queryList = rideRepository.findAllEndedRidesOfDriver(userId);
        else if (userRole.equals("ROLE_ADMIN"))
            queryList = rideRepository.findAllEndedRides();
        else
            queryList = rideRepository.findAllEndedRidesOfClient(userId);


        List<Ride> pageList = queryList.stream()
                .skip(page.getPageSize() * page.getPageNumber())
                .limit(page.getPageSize())
                .collect(Collectors.toList());

        if (sort.equals("startTime") && order.equals("desc")) {
            pageList.sort(Comparator.comparing(Ride::getStartTime).reversed());
        }
        else if (sort.equals("startTime") && order.equals("asc")) {
            pageList.sort(Comparator.comparing(Ride::getStartTime));
        }
        else if (sort.equals("calculatedDuration") && order.equals("desc")) {
            pageList.sort(Comparator.comparing(Ride::getCalculatedDuration).reversed());
        }
        else if (sort.equals("calculatedDuration") && order.equals("asc")) {
            pageList.sort(Comparator.comparing(Ride::getCalculatedDuration));
        }
        else if (sort.equals("price") && order.equals("desc")) {
            pageList.sort(Comparator.comparing(Ride::getPrice).reversed());
        }
        else if (sort.equals("price") && order.equals("asc")) {
            pageList.sort(Comparator.comparing(Ride::getPrice));
        }
        else {
            pageList.sort(Comparator.comparing(Ride::getStartTime).reversed());        // ovde moze i ascending
        }

        Page<Ride> retPage = new PageImpl<>(pageList, page, queryList.size());

        return retPage;
    }

    public Optional<Ride> findRideById(Long rideId) {
        return this.rideRepository.findById(rideId);
    }

    public ClientRide findClientRide(Long rideId, Long clientId) {
        ClientRide clientRide = clientRideRepository.findClientRideByRideId(rideId, clientId);
        return clientRide;
    }

}
