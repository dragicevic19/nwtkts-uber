package com.nwtkts.uber.controller;

import com.nwtkts.uber.dto.RideDTO;
import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.Vehicle;
import com.nwtkts.uber.service.RideService;
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
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public RideController(RideService rideService, SimpMessagingTemplate simpMessagingTemplate) {
        this.rideService = rideService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PostMapping(
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<RideDTO> createRide(@RequestBody RideDTO rideDTO) {
        Ride ride = this.rideService.createRide(new Ride(rideDTO), new Vehicle(rideDTO.getVehicle()));
        RideDTO returnRideDTO = new RideDTO(ride);
        this.simpMessagingTemplate.convertAndSend("/map-updates/new-ride", returnRideDTO);
        return new ResponseEntity<>(returnRideDTO, HttpStatus.OK);
    }

    @PutMapping(
            path = "/{id}",
            produces = "application/json"
    )
    public ResponseEntity<RideDTO> changeRide(@PathVariable("id") Long id) {
        Ride ride = this.rideService.changeRide(id);
        RideDTO returnRideDTO = new RideDTO(ride);
        this.simpMessagingTemplate.convertAndSend("/map-updates/ended-ride", returnRideDTO);
        return new ResponseEntity<>(returnRideDTO, HttpStatus.OK);
    }

    @GetMapping(
            produces = "application/json"
    )
    public ResponseEntity<List<RideDTO>> getRides() {
        List<Ride> rides = this.rideService.getRides();
        List<RideDTO> rideDTOs = new ArrayList<>();
        for (Ride ride : rides) {
            rideDTOs.add(new RideDTO(ride));
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