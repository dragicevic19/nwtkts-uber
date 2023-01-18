package com.nwtkts.uber.controller;
import com.nwtkts.uber.dto.ActiveDriverDTO;
import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.service.DriverService;
import com.nwtkts.uber.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

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
}