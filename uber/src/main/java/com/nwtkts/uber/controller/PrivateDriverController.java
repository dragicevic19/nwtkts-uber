package com.nwtkts.uber.controller;

import com.nwtkts.uber.dto.ActiveRideDTO;
import com.nwtkts.uber.dto.RideCancelationDTO;
import com.nwtkts.uber.dto.RideDTO;
import com.nwtkts.uber.dto.UserProfile;
import com.nwtkts.uber.exception.BadRequestException;
import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.RideStatus;
import com.nwtkts.uber.service.DriverService;
import com.nwtkts.uber.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/driver", produces = MediaType.APPLICATION_JSON_VALUE)
public class PrivateDriverController {

    @Autowired
    private DriverService driverService;
    @Autowired
    private RideService rideService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping(
            path = "/startRide",
            produces = "application/json"
    )
    public ResponseEntity<?> startRide(@RequestBody Long rideId) {
        Ride ride = this.rideService.getRideById(rideId);
        ride = this.rideService.startRide(ride);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @PutMapping(path = "/cancelRide", produces = "application/json")
    public ResponseEntity<?> cancelRideDriver(Principal user, @RequestBody RideCancelationDTO rideCancelationDTO) {

        Driver driver = this.driverService.findByEmail(user.getName());
        if (driver == null) throw new BadRequestException("Bad user");

        Ride ride = this.rideService.cancelRideDriver(driver, rideCancelationDTO);
        if (ride.getRideStatus() == RideStatus.SCHEDULED) {
            this.simpMessagingTemplate.convertAndSend("/map-updates/driver-ending-ride",
                    new ActiveRideDTO(ride, driver.getId(), ride.getClientsInfo()));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @PutMapping(path = "/endRide", produces = "application/json")
    public ResponseEntity<?> finishRideDriver(Principal user, @RequestBody Long rideId) {

        Driver driver = this.driverService.findByEmail(user.getName());
        if (driver == null) throw new BadRequestException("Bad user");

        List<Ride> thisAndNextRide = this.rideService.finishRideDriver(driver, rideId);
        Ride ride = thisAndNextRide.get(0);
        this.simpMessagingTemplate.convertAndSend("/map-updates/driver-ending-ride", new ActiveRideDTO(ride, ride.getClientsInfo()));
        this.simpMessagingTemplate.convertAndSend("/map-updates/ended-ride", new RideDTO(ride, ride.getClientsInfo()));
        if (thisAndNextRide.size() > 1) {
            this.simpMessagingTemplate.convertAndSend("/map-updates/change-drivers-ride-status",
                    new ActiveRideDTO(thisAndNextRide.get(1), thisAndNextRide.get(1).getClientsInfo()));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @PutMapping(path = "/changeActive", produces = "application/json")
    public ResponseEntity<UserProfile> finishRideDriver(Principal user, @RequestBody Boolean active) {

        Driver driver = this.driverService.findDetailedByEmail(user.getName());
        if (driver == null) throw new BadRequestException("Bad user");

        driver = this.driverService.changeActive(driver, active);

        return new ResponseEntity<>(new UserProfile(driver), HttpStatus.OK);
    }


}