package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.model.*;
import com.nwtkts.uber.repository.DriverRepository;
import com.nwtkts.uber.repository.RideRepository;
import com.nwtkts.uber.service.FindDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FindDriverServiceImpl implements FindDriverService {

    private static final int MAX_WORKING_HOURS = 8;

    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private RideRepository rideRepository;


    @Override
    public Driver searchDriver(Ride ride) {
        if (this.driverRepository.findAllByActive(true).size() == 0)
            return null;

        Driver driver = searchAvailableDriversForRide(ride);
        if (driver != null) return driver;

        driver = searchActiveDriversForRide(ride);
        return driver;
    }

    private Driver searchActiveDriversForRide(Ride ride) { // trazim aktivne koji trenutno imaju voznju
        Driver retDriver = null;
        List<Driver> allBusyDrivers = this.driverRepository.findAllDetailedByActiveAndAvailable(true, false);
        List<Driver> availableForNextRide = new ArrayList<>();
        for (Driver driver : allBusyDrivers) {
            if (driver.getNextRideId() == null) {
                if (checkDriverWorkingHours(driver) && checkIfDriverIsCompatibleWithRequest(ride, driver) &&
                        checkIfDriverIsMakingItBeforeHisScheduledRide(ride, driver)) {
                    availableForNextRide.add(driver);
                }
            }
        }
        if (availableForNextRide.size() > 0) {
            retDriver = findDriverClosestToEndRide(availableForNextRide);
            if (retDriver != null && ride.getScheduledFor() == null)
                retDriver.setNextRideId(ride.getId());    // vozace za scheduled voznje ne oznacavam sa nextRideId
        }
        return retDriver;
    }


    private Driver searchAvailableDriversForRide(Ride ride) {
        Driver retDriver = null;
        List<Driver> allAvailableDrivers = this.driverRepository.findAllDetailedByAvailable(true);
        List<Driver> actuallyAvailable = new ArrayList<>();

        for (Driver driver : allAvailableDrivers) {
            if (checkDriverWorkingHours(driver) && checkIfDriverIsCompatibleWithRequest(ride, driver) &&
                    checkIfDriverIsMakingItBeforeHisScheduledRide(ride, driver)) {
                actuallyAvailable.add(driver);
            }
        }
        if (actuallyAvailable.size() > 0)
            retDriver = findClosestDriverToLocation(actuallyAvailable, ride.getStartingLocation());

        return retDriver;
    }


    private boolean checkIfDriverIsMakingItBeforeHisScheduledRide(Ride newRide, Driver driver) {
        List<Ride> scheduledRidesForDriver = this.rideRepository.findAllDetailedByRideStatusAndDriver_Id(RideStatus.SCHEDULED, driver.getId());
        LocalDateTime newRideStartTime = LocalDateTime.now();

        if (newRide.getScheduledFor() != null) newRideStartTime = newRide.getScheduledFor();

        long newRideDuration = Math.round(newRide.getCalculatedDuration()); // 8 = vreme do prve pickup lokacije + vreme do scheduled pickup lokacije
        LocalDateTime projectedEndingTimeOfNewRide = newRideStartTime.plusMinutes(newRideDuration).plusMinutes(8);

        for (Ride scheduledRide : scheduledRidesForDriver) {
            LocalDateTime projectedEndingTimeOfScheduledRide = scheduledRide.getScheduledFor().plusMinutes(Math.round(scheduledRide.getCalculatedDuration())).plusMinutes(8);

            if (projectedEndingTimeOfNewRide.isAfter(scheduledRide.getScheduledFor())) {
                if (projectedEndingTimeOfScheduledRide.isAfter(newRideStartTime))
                    return false;
            }
        }
        return true;
    }

    private boolean checkIfDriverIsCompatibleWithRequest(Ride ride, Driver driver) {
        if (driver.getVehicle().getType().getId() != ride.getRequestedVehicleType().getId()) return false;
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

    private Ride getCurrentRideForDriver(Driver driver) {
        List<RideStatus> acceptableStatuses = new ArrayList<>(Arrays.asList(RideStatus.STARTED, RideStatus.WAITING_FOR_CLIENT, RideStatus.TO_PICKUP));
        return this.rideRepository.findByDriver_IdAndRideStatusIn(driver.getId(), acceptableStatuses);
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
        long sumOfSeconds = 0;

        for (DriverActivity activity : driver.getActivities()) {
            if (activity.getStartTime().isBefore(from) && activity.getEndTime().isBefore(from))
                continue; // ne zanima me
            long seconds;
            if (activity.getStartTime().equals(activity.getEndTime())) {  // didn't change active status yet (or logout)
                seconds = Math.abs(ChronoUnit.SECONDS.between(activity.getStartTime(), LocalDateTime.now()));
            }
            else if (activity.getStartTime().isBefore(from) && activity.getEndTime().isAfter(from)) {
                seconds = Math.abs(ChronoUnit.SECONDS.between(activity.getEndTime(), from));  // racunam samo u prethodna 24 sata?
            } else {
                seconds = Math.abs(ChronoUnit.SECONDS.between(activity.getEndTime(), activity.getStartTime()));
            }
            sumOfSeconds += seconds;
        }
        return sumOfSeconds / 3600 < MAX_WORKING_HOURS;
    }
}
