package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.dto.RideRequest;
import com.nwtkts.uber.dto.UserProfile;
import com.nwtkts.uber.exception.NotFoundException;
import com.nwtkts.uber.model.*;
import com.nwtkts.uber.repository.ClientRepository;
import com.nwtkts.uber.repository.DriverRepository;
import com.nwtkts.uber.repository.RideRepository;
import com.nwtkts.uber.repository.VehicleRepository;
import com.nwtkts.uber.service.ClientService;
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

    private static final double PRICE_PER_KM = 1.3;
    private static final int MAX_WORKING_HOURS = 8;
    private final RideRepository rideRepository;
    private final VehicleRepository vehicleRepository;
    private final ClientRepository clientRepository;
    private final DriverRepository driverRepository;
    private final ClientService clientService;

    @Autowired
    public RideServiceImpl(RideRepository rideRepository, VehicleRepository vehicleRepository,
                           ClientRepository clientRepository, ClientService clientService, DriverRepository driverRepository) {
        this.rideRepository = rideRepository;
        this.vehicleRepository = vehicleRepository;
        this.clientRepository = clientRepository;
        this.driverRepository = driverRepository;
        this.clientService = clientService;
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
        }
        else {
            driver.setAvailable(true);
            driver.setNextRideId(null);
        }
        this.driverRepository.save(driver);
        return ride;
    }

    private void goForNextRide(Driver driver) {
        Ride nextRide = this.rideRepository.findDetailedById(driver.getNextRideId()).orElseThrow(()
                -> new NotFoundException("New ride doesn't exist!"));
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
    public List<Ride> getDetailedRides(){
        return this.rideRepository.findDetailedByRideStatusOrRideStatus(RideStatus.STARTED, RideStatus.CRUISING);
    }

    @Override
    public void deleteAllRides() {
        this.rideRepository.deleteAll();
    }

    @Override
    @Transactional
    public Ride makeRideRequest(Client client, RideRequest rideRequest) {
        Ride newRide = new Ride();

        double price = rideRequest.getSelectedRoute().getDistance() * PRICE_PER_KM;
        price += rideRequest.getVehicleType().getAdditionalPrice();
        double pricePerPerson = price / (rideRequest.getAddedFriends().size() + 1);

        newRide.setPrice(price);
        newRide.setScheduled(rideRequest.isScheduled());
        newRide.setCalculatedDuration(rideRequest.getSelectedRoute().getDuration());
        newRide.setRideStatus(RideStatus.WAITING);
        newRide.setRouteJSON(rideRequest.getSelectedRoute().getLegsStr());
        newRide.setBabiesOnRide(rideRequest.isBabies());
        newRide.setPetsOnRide(rideRequest.isPets());

        newRide.setStartingLocation(
                new Location(rideRequest.getSelectedRoute().getStartingLatitude(), rideRequest.getSelectedRoute().getStartingLongitude()));
        newRide.setEndingLocation(
                new Location(rideRequest.getSelectedRoute().getEndingLatitude(), rideRequest.getSelectedRoute().getEndingLongitude()));

        this.clientService.makePayment(client, pricePerPerson);
        newRide.setClientsInfo(makeClientsInfos(client, rideRequest));

        this.rideRepository.save(newRide);

        if (areAllClientsFinishedPayment(newRide)) {

            Driver driver = searchDriver(newRide, rideRequest);
            if (driver != null) {
                this.driverFounded(newRide, driver);
            } else {
                newRide.setRideStatus(RideStatus.CANCELED);
                this.clientService.refundForCanceledRide(client, pricePerPerson);
            }
            this.rideRepository.save(newRide);
        }

        return newRide;
    }

    private void driverFounded(Ride newRide, Driver driver) {
        if (driver.getNextRideId() == null) {   // nasao je odmah slobodnog vozaca
            driver.setAvailable(false);
            newRide.setRideStatus(RideStatus.STARTED);
            newRide.setStartTime(LocalDateTime.now());
        }
        // u slucaju da je nasao zauzetog koji je slobodan za sledecu voznju: rideStatus stavljam kad zavrsi trenutnu voznju
        newRide.setDriver(driver);
        newRide.setVehicle(driver.getVehicle());
        this.driverRepository.save(driver);
    }


    private Driver searchDriver(Ride ride, RideRequest rideRequest) {
        if (this.driverRepository.findAllByActive(true).size() == 0)
            return null;

        Driver driver = searchAvailableDriversForRide(ride, rideRequest);
        if (driver != null) return driver;

        driver = searchActiveDriversForRide(ride, rideRequest);
        return driver;
    }

    private Driver searchActiveDriversForRide(Ride ride, RideRequest rideRequest) { // trazim aktivne koji trenutno imaju voznju
        Driver retDriver = null;
        List<Driver> allBusyDrivers = this.driverRepository.findAllDetailedByActiveAndAvailable(true, false);
        List<Driver> availableForNextRide = new ArrayList<>();
        for (Driver driver : allBusyDrivers) {
            if (driver.getNextRideId() == null) {
                if (checkDriverWorkingHours(driver) && checkIfDriverIsCompatibleWithRequest(ride, driver, rideRequest)) {
                    availableForNextRide.add(driver);
                }
            }
        }
        if (availableForNextRide.size() > 0) {
            retDriver = findDriverClosestToEndRide(availableForNextRide);
            if (retDriver != null) retDriver.setNextRideId(ride.getId());
        }
        return retDriver;
    }


    private Driver searchAvailableDriversForRide(Ride ride, RideRequest rideRequest) {
        Driver retDriver = null;
        List<Driver> allAvailableDrivers = this.driverRepository.findAllDetailedByAvailable(true);
        List<Driver> actuallyAvailable = new ArrayList<>();

        for (Driver driver : allAvailableDrivers) {
            if (checkDriverWorkingHours(driver) && checkIfDriverIsCompatibleWithRequest(ride, driver, rideRequest)) {
                actuallyAvailable.add(driver);
            }
        }
        if (actuallyAvailable.size() > 0)
            retDriver = findClosestDriverToLocation(actuallyAvailable, ride.getStartingLocation());

        return retDriver;
    }

    private boolean checkIfDriverIsCompatibleWithRequest(Ride ride, Driver driver, RideRequest rideRequest) {
        if (driver.getVehicle().getType().getId() != rideRequest.getVehicleType().getId()) return false;
        if (ride.isBabiesOnRide() && !driver.getVehicle().getBabiesAllowed()) return false;
        if (ride.isPetsOnRide() && !driver.getVehicle().getBabiesAllowed()) return false;

        return true;
    }

    private Driver findClosestDriverToLocation(List<Driver> actuallyAvailable, Location location) {
        Driver bestDriver = null;
        double bestDistance = Double.MAX_VALUE;
        for (Driver driver : actuallyAvailable) {
            double distance = calculateDistance(
                    driver.getVehicle().getCurrentLocation().getLatitude(), driver.getVehicle().getCurrentLocation().getLongitude(),
                    location.getLatitude(), location.getLongitude());

            if (distance < bestDistance) {
                bestDistance = distance;
                bestDriver = driver;
            }
        }
        return bestDriver;
    }

    private Driver findDriverClosestToEndRide(List<Driver> availableForNextRide) {
        Driver bestDriver = null;
        double bestDistance = Double.MAX_VALUE;

        for (Driver driver : availableForNextRide) {
            Ride currentRide = getCurrentRideForDriver(driver);
            double distance = calculateDistance(
                    driver.getVehicle().getCurrentLocation().getLatitude(), driver.getVehicle().getCurrentLocation().getLongitude(),
                    currentRide.getEndingLocation().getLatitude(), currentRide.getEndingLocation().getLongitude());

            if (distance < bestDistance) {
                bestDistance = distance;
                bestDriver = driver;
            }
        }
        return bestDriver;
    }

    public Ride getCurrentRideForDriver(Driver driver) {
        return this.rideRepository.findByRideStatusAndDriver_Id(RideStatus.STARTED, driver.getId());
    }

    private double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        final double R = 6371e3;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        distance = Math.pow(distance, 2);
        return Math.sqrt(distance);
    }

    private boolean checkDriverWorkingHours(Driver driver) {
        LocalDateTime from = LocalDateTime.now().minusHours(24);
        long sumOfHours = 0;

        for (DriverActivity activity : driver.getActivities()) {
            if (activity.getStartTime().isBefore(from) && activity.getEndTime().isBefore(from))
                continue; // ne zanima me
            long hours;
            if (activity.getStartTime().isBefore(from) && activity.getEndTime().isAfter(from)) {
                hours = ChronoUnit.HOURS.between(activity.getEndTime(), from);  // racunam samo u prethodna 24 sata?
            } else {
                hours = ChronoUnit.HOURS.between(activity.getEndTime(), activity.getStartTime());
            }
            sumOfHours += hours;
        }
        return sumOfHours < MAX_WORKING_HOURS;
    }

    private boolean areAllClientsFinishedPayment(Ride ride) {
        for (ClientRide clientRide : ride.getClientsInfo()) {
            if (!clientRide.isClientPaid()) return false;
        }
        return true;
    }

    private List<ClientRide> makeClientsInfos(Client client, RideRequest rideRequest) {
        List<ClientRide> clientsInfo = new ArrayList<>();
        clientsInfo.add(new ClientRide(client, true));

        for (UserProfile user : rideRequest.getAddedFriends()) {
            Client c = clientRepository.findById(user.getId()).orElseThrow(() -> new NotFoundException("Added friend doesn't exist"));
            clientsInfo.add(new ClientRide(c));
        }
        return clientsInfo;
    }
}
