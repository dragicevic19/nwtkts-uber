package com.nwtkts.uber.controller;

import com.nwtkts.uber.dto.FakeRideDTO;
import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.Location;
import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.Vehicle;
import com.nwtkts.uber.service.DriverService;
import com.nwtkts.uber.service.RideService;
import com.nwtkts.uber.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/ride")
public class RideController {

    private final RideService rideService;
    private final VehicleService vehicleService;
    private final DriverService driverService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public RideController(RideService rideService, VehicleService vehicleService,
                          DriverService driverService, SimpMessagingTemplate simpMessagingTemplate) {
        this.rideService = rideService;
        this.vehicleService = vehicleService;
        this.driverService = driverService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PostMapping(
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<FakeRideDTO> createRide(@RequestBody FakeRideDTO rideDTO) {
        Vehicle vehicle = this.vehicleService.getVehicleForDriverFake(rideDTO);
        Driver driver = this.driverService.getDriverForVehicle(vehicle);
        if (driver == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Ride ride = this.rideService.createRide(new Ride(rideDTO), vehicle, driver);
        FakeRideDTO returnRideDTO = new FakeRideDTO(ride);
        this.simpMessagingTemplate.convertAndSend("/map-updates/new-ride", returnRideDTO);
        return new ResponseEntity<>(returnRideDTO, HttpStatus.OK);
    }

    @PutMapping(
            path = "/{id}",
            produces = "application/json"
    )
    public ResponseEntity<FakeRideDTO> changeRide(@PathVariable("id") Long id) {
        Ride ride = this.rideService.changeRide(id);
        FakeRideDTO returnRideDTO = new FakeRideDTO(ride);
        this.simpMessagingTemplate.convertAndSend("/map-updates/ended-ride", returnRideDTO);
        return new ResponseEntity<>(returnRideDTO, HttpStatus.OK);
    }

    @GetMapping(
            produces = "application/json"
    )
    public ResponseEntity<List<FakeRideDTO>> getRides() {
        List<Ride> rides = this.rideService.getRides();
        List<FakeRideDTO> rideDTOs = new ArrayList<>();
        for (Ride ride : rides) {
            rideDTOs.add(new FakeRideDTO(ride));
        }
        return new ResponseEntity<>(rideDTOs, HttpStatus.OK);
    }

    @DeleteMapping(
            produces = "text/plain"
    )
    public ResponseEntity<String> deleteAllVehicles() {
        this.rideService.deleteAllRides();
        this.simpMessagingTemplate.convertAndSend("/map-updates/delete-all-rides", "Delete all rides");
        return new ResponseEntity<>("All rides deleted!", HttpStatus.OK);
    }
}