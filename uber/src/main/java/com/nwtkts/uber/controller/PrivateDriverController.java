package com.nwtkts.uber.controller;

import com.nwtkts.uber.dto.DriversRidesDTO;
import com.nwtkts.uber.dto.RideDTO;
import com.nwtkts.uber.exception.BadRequestException;
import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.service.DriverService;
import com.nwtkts.uber.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/driver", produces = MediaType.APPLICATION_JSON_VALUE)
public class PrivateDriverController {

    @Autowired
    private DriverService driverService;
    @Autowired
    private RideService rideService;

    @PostMapping(
            path = "/startRide",
            produces = "application/json"
    )
    public ResponseEntity<?> startRide(@RequestBody Long rideId) {
        Ride ride = this.rideService.getRideById(rideId);
        ride = this.rideService.startRide(ride);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(
            path = "/myActiveRides",
            produces = "application/json"
    )
    public ResponseEntity<List<DriversRidesDTO>> getActiveRidesForLoggedInDriver(Principal user) {
        Driver driver = this.driverService.findDetailedByEmail(user.getName());
        if (driver == null)
            throw new BadRequestException("Not allowed for this user");

        List<Ride> rides = this.rideService.getRidesForDriver(driver.getId());
        List<DriversRidesDTO> driversRidesDTOS = new ArrayList<>();
        for (Ride ride : rides) {
            driversRidesDTOS.add(new DriversRidesDTO(ride));
        }
        return new ResponseEntity<>(driversRidesDTOS, HttpStatus.OK);
    }
}