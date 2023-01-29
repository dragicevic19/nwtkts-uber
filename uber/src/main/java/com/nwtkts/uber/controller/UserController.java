package com.nwtkts.uber.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import com.nwtkts.uber.dto.ChangePasswordRequest;
import com.nwtkts.uber.dto.ActiveRideDTO;
import com.nwtkts.uber.dto.EditUserRequestDTO;
import com.nwtkts.uber.dto.UserProfile;
import com.nwtkts.uber.exception.NotFoundException;
import com.nwtkts.uber.model.Client;
import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.User;
import com.nwtkts.uber.service.EditUserRequestService;
import com.nwtkts.uber.service.RideService;
import com.nwtkts.uber.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private EditUserRequestService editUserRequestService;
    @Autowired
    private RideService rideService;


    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public User loadById(@PathVariable Long userId) {
        return this.userService.findById(userId);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> loadAll() {
        return this.userService.findAll();
    }

    @GetMapping("/whoami")
    public ResponseEntity<UserProfile> loggedInUser(Principal user) {
        User loggedInUser = this.userService.findByEmail(user.getName());
        if (loggedInUser == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new UserProfile(loggedInUser), HttpStatus.OK);
    }

    @PutMapping("/editInfo")
    public ResponseEntity<UserProfile> editUserInfo(Principal user, @RequestBody UserProfile editedUser) {
        User loggedInUser = this.userService.findByEmail(user.getName());
        if (loggedInUser == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        User editingUser = this.userService.findByEmail(editedUser.getEmail());
        if (editingUser == null || !editingUser.getId().equals(loggedInUser.getId())) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        User newUser = this.userService.editUserInfo(loggedInUser, editedUser);

        if (newUser == null) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new UserProfile(newUser), HttpStatus.OK);
    }

    @PutMapping("/changeProfilePic")
    public ResponseEntity<UserProfile> editUserInfo(Principal user, @RequestBody String picUrl) {
        User loggedInUser = this.userService.findByEmail(user.getName());
        if (loggedInUser == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        User newUser = this.userService.changeProfilePicture(loggedInUser, picUrl);
        if (newUser == null) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new UserProfile(newUser), HttpStatus.OK);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<UserProfile> changePassword(Principal user, @RequestBody ChangePasswordRequest request) {
        User loggedInUser = this.userService.findByEmail(user.getName());
        if (loggedInUser == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        User updatedUser = this.userService.changePassword(loggedInUser, request);
        if (updatedUser == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new UserProfile(updatedUser), HttpStatus.OK);
    }

    @PostMapping(
            path = "/createNotification",
            produces = "application/json"
    )
    public ResponseEntity<EditUserRequestDTO> createNotification(@RequestBody EditUserRequestDTO requestDTO) {
        EditUserRequestDTO eur = editUserRequestService.createNotification(requestDTO);
        return new ResponseEntity<>(eur, HttpStatus.OK);
    }

    @GetMapping(
            path = "/myActiveRides",
            produces = "application/json"
    )
    public ResponseEntity<List<ActiveRideDTO>> getActiveRidesForLoggedIn(Principal user) {
        User loggedIn = this.userService.findByEmail(user.getName());
        if (loggedIn == null)
            throw new NotFoundException("User doesn't exist");

        List<Ride> rides = new ArrayList<>();
        if (loggedIn instanceof Driver) {
            rides = this.rideService.getActiveRidesForDriver(loggedIn.getId());
        }
        else if (loggedIn instanceof Client) {
            rides = this.rideService.getActiveRidesForClient((Client) loggedIn);
        }

        List<ActiveRideDTO> activeRideDTOS = new ArrayList<>();
        for (Ride ride : rides) {
            activeRideDTOS.add(new ActiveRideDTO(ride));
        }
        return new ResponseEntity<>(activeRideDTOS, HttpStatus.OK);
    }
}