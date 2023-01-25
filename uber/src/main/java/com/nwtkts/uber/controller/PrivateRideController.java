package com.nwtkts.uber.controller;

import com.nwtkts.uber.dto.*;
import com.nwtkts.uber.exception.BadRequestException;
import com.nwtkts.uber.model.*;
import com.nwtkts.uber.service.ClientService;
import com.nwtkts.uber.service.DriverService;
import com.nwtkts.uber.service.RideService;
import com.nwtkts.uber.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/ride")
public class PrivateRideController {

    private UserService userService;
    private RideService rideService;
    private ClientService clientService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private DriverService driverService;

    @Autowired
    public PrivateRideController(UserService userService, RideService rideService,
                                 SimpMessagingTemplate simpMessagingTemplate, ClientService clientService,
                                 DriverService driverService) {
        this.userService = userService;
        this.rideService = rideService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.clientService = clientService;
        this.driverService = driverService;
    }

    @PostMapping(
            path = "/newRideRequest",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<RideDTO> newRideRequest(Principal user, @RequestBody RideRequest rideRequest) {

        Client client = this.clientService.findDetailedByEmail(user.getName());
        if (client == null) throw new BadRequestException("Not allowed for this user");

        Ride newRide = this.rideService.makeRideRequest(client, rideRequest);
        RideDTO returnRideDTO = new RideDTO(newRide);

        if (newRide.getRideStatus() == RideStatus.TO_PICKUP || newRide.getRideStatus() == RideStatus.WAITING_FOR_DRIVER_TO_FINISH ||
                (newRide.getRideStatus() == RideStatus.SCHEDULED && newRide.getDriver() != null)) {
            this.simpMessagingTemplate.convertAndSend("/map-updates/new-ride-for-driver", new DriversRidesDTO(newRide));
        }
        if (newRide.getRideStatus() == RideStatus.WAITING_FOR_PAYMENT) {
            this.simpMessagingTemplate.convertAndSend("/map-updates/new-split-fare-req", new ClientsSplitFareRideDTO(newRide));
        }

        return new ResponseEntity<>(returnRideDTO, HttpStatus.OK);
    }

    @PostMapping(
            path = "/favRoute",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> addFavRoute(Principal user, @RequestBody FavRouteDTO routeRequest) {

        Client client = this.clientService.findDetailedByEmail(user.getName());
        if (client == null) throw new BadRequestException("Not allowed for this user");

        Route newRoute = this.rideService.addRouteToFavorites(client, routeRequest);
        return new ResponseEntity<>(newRoute, HttpStatus.OK);
    }
}
