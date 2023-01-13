package com.nwtkts.uber.controller;

import com.nwtkts.uber.dto.FakeRideDTO;
import com.nwtkts.uber.dto.RideReqResponse;
import com.nwtkts.uber.dto.RideRequest;
import com.nwtkts.uber.exception.BadRequestException;
import com.nwtkts.uber.model.Client;
import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.RideStatus;
import com.nwtkts.uber.model.User;
import com.nwtkts.uber.service.RideService;
import com.nwtkts.uber.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/ride")
public class PrivateRideController {

    private UserService userService;
    private RideService rideService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public PrivateRideController(UserService userService, RideService rideService,
                                 SimpMessagingTemplate simpMessagingTemplate) {
        this.userService = userService;
        this.rideService = rideService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PostMapping(
            path = "/newRideRequest",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<FakeRideDTO> newRideRequest(Principal user, @RequestBody RideRequest rideRequest) {
        User u = this.userService.findByEmail(user.getName());
        if (u == null) throw new BadRequestException("User is not logged in");

        if (u instanceof Client) {
            Client client = (Client) u;
            Ride newRide = this.rideService.makeRideRequest(client, rideRequest);
            if (newRide.getRideStatus() == RideStatus.STARTED) {
//                this.simpMessagingTemplate.convertAndSend("/map-updates/new-ride", returnRideDTO);
            }
            return new ResponseEntity<>(new FakeRideDTO(newRide), HttpStatus.OK);
        }
        throw new BadRequestException("User is not client");
    }
}
