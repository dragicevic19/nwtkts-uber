package com.nwtkts.uber.controller;

import com.nwtkts.uber.dto.NotificationDTO;
import com.nwtkts.uber.dto.RideDTO;
import com.nwtkts.uber.exception.NotFoundException;
import com.nwtkts.uber.model.*;
import com.nwtkts.uber.service.DriverService;
import com.nwtkts.uber.service.RideService;
import com.nwtkts.uber.service.UserService;
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
    private final UserService userService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public RideController(RideService rideService, VehicleService vehicleService,
                          DriverService driverService, UserService userService, SimpMessagingTemplate simpMessagingTemplate) {
        this.rideService = rideService;
        this.vehicleService = vehicleService;
        this.driverService = driverService;
        this.userService = userService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PostMapping(
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<RideDTO> createRide(@RequestBody RideDTO rideDTO) {
        Vehicle vehicle = this.vehicleService.getVehicleForDriverFake(rideDTO);
        Driver driver = this.driverService.getDriverForVehicle(vehicle);
        if (driver == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Ride ride = this.rideService.createRide(new Ride(rideDTO), vehicle, driver);
        RideDTO returnRideDTO = new RideDTO(ride);
        this.simpMessagingTemplate.convertAndSend("/map-updates/new-ride", returnRideDTO);
        return new ResponseEntity<>(returnRideDTO, HttpStatus.OK);
    }

    @PutMapping(
            path = "/{id}",
            produces = "application/json"
    )
    public ResponseEntity<RideDTO> endRide(@PathVariable("id") Long id) {
        Ride ride = this.rideService.endRide(id);
        RideDTO returnRideDTO = new RideDTO(ride, ride.getClientsInfo());
        this.simpMessagingTemplate.convertAndSend("/map-updates/ended-ride", returnRideDTO);
        return new ResponseEntity<>(returnRideDTO, HttpStatus.OK);
    }

    @PutMapping(
            path = "fake/{id}",
            produces = "application/json"
    )
    public ResponseEntity<RideDTO> endFakeRide(@PathVariable("id") Long id) {
        Ride ride = this.rideService.endFakeRide(id);
        RideDTO returnRideDTO = new RideDTO(ride);
        this.simpMessagingTemplate.convertAndSend("/map-updates/ended-ride", returnRideDTO);
        return new ResponseEntity<>(returnRideDTO, HttpStatus.OK);
    }

    @GetMapping(
            produces = "application/json"
    )
    public ResponseEntity<List<RideDTO>> getRides() {
        List<Ride> rides = this.rideService.getDetailedRides();
        List<RideDTO> rideDTOs = new ArrayList<>();
        for (Ride ride : rides) {
            rideDTOs.add(new RideDTO(ride, ride.getClientsInfo()));
        }
        return new ResponseEntity<>(rideDTOs, HttpStatus.OK);
    }

    @GetMapping(
            path = "/driver/{id}",
            produces = "application/json"
    )
    public ResponseEntity<RideDTO> getRideForDriver(@PathVariable Long id) { // ovo pozivam iz locusta kad pravim koordinate za pravu voznju
        Ride ride = this.rideService.getDetailedActiveRideForDriver(id);                  // zato /map-updates/new-ride
        if (ride == null) throw new NotFoundException("Ride doesn't exist!");
        RideDTO returnRideDTO = new RideDTO(ride, ride.getClientsInfo());
        this.simpMessagingTemplate.convertAndSend("/map-updates/new-ride", returnRideDTO);
        return new ResponseEntity<>(returnRideDTO, HttpStatus.OK);
    }

    @GetMapping(
            path = "/scheduled",
            produces = "application/json"
    )
    public ResponseEntity<List<RideDTO>> checkScheduledRides() {
        List<Ride> scheduledRidesInNext15Minutes = this.rideService.checkScheduledRides();
        List<RideDTO> ridesForLocust = new ArrayList<>();

        for (Ride ride : scheduledRidesInNext15Minutes) {
            ridesForLocust.add(new RideDTO(ride));
        }
        return new ResponseEntity<>(ridesForLocust, HttpStatus.OK);
    }

    @GetMapping(
            path = "/scheduled/notify/{id}"
    )
    public ResponseEntity<?> sendNotificationToClientAboutScheduledRide(@PathVariable Long id) {
        Ride ride = this.rideService.getDetailedRideById(id);
        String notification = this.rideService.generateNotificationForClientsScheduledRide(ride);

        if (notification != null) {
            this.simpMessagingTemplate.convertAndSend("/map-updates/client-notifications-scheduled-ride-in",
                    new NotificationDTO(notification, ride.getClientsInfo()));
        }

        return new ResponseEntity<>(HttpStatus.OK);
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