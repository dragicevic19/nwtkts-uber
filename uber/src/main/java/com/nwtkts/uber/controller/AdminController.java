package com.nwtkts.uber.controller;

import com.nwtkts.uber.dto.DriverRegistrationDTO;
import com.nwtkts.uber.dto.EditUserRequestDTO;
import com.nwtkts.uber.dto.UserProfile;
import com.nwtkts.uber.model.*;
import com.nwtkts.uber.repository.EditUserRequestRepository;
import com.nwtkts.uber.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private DriverService driverService;
    @Autowired
    private VehicleTypeService vehicleTypeService;
    @Autowired
    private EditUserRequestService editUserRequestService;



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/newDriver")
    public ResponseEntity<Boolean> newDriverAndVehicle(@RequestBody DriverRegistrationDTO driverRegistrationDTO) {

        driverRegistrationDTO.setEmail(driverRegistrationDTO.getEmail().toLowerCase(Locale.ROOT));
        User existUser = this.userService.findByEmail(driverRegistrationDTO.getEmail());

        if (existUser != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Driver driver = this.driverService.register(driverRegistrationDTO);
        if (driver != null)
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        else
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping(
            path = "/getAllUsers",
            produces = "application/json"
    )
    public ResponseEntity<List<UserProfile>> getAllUsers() {
        List<UserProfile> returnList = new ArrayList<>();
        try {
            List<User> users = userService.findAll();
            for (User u : users) {
                        returnList.add(new UserProfile(u));
            }
            return new ResponseEntity<>(returnList, HttpStatus.OK);
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(returnList, HttpStatus.NOT_FOUND);
    }

    @PutMapping(
            path = "/updateUser",
            produces = "application/json"
    )
    public ResponseEntity<UserProfile> updateUserFromUserProfile(@RequestBody UserProfile user) {
        User foundUser = userService.updateUserFromUserProfile(user);
        return new ResponseEntity<>(new UserProfile(foundUser), HttpStatus.OK);
    }

    @GetMapping(
            path = "/getAllVehicleTypes",
            produces = "application/json"
    )
    public ResponseEntity<List<VehicleType>> getAllVehicleTypes() throws AccessDeniedException {
        List<VehicleType> vehicleTypes = vehicleTypeService.findAll();
        return new ResponseEntity<>(vehicleTypes, HttpStatus.OK);
    }
    @PostMapping(
            path = "/createDriver",
            produces = "application/json"
    )
    public ResponseEntity<DriverRegistrationDTO> createDriver(@RequestBody DriverRegistrationDTO driverDTO) {
        driverService.register(driverDTO);
        return new ResponseEntity<>(driverDTO, HttpStatus.OK);
    }

    @GetMapping(
            path = "/getNotifications",
            produces = "application/json"
    )
    public ResponseEntity<List<EditUserRequestDTO>> getNotifications() {
        List<EditUserRequest> allRequests = editUserRequestService.findByStatus("pending");
        List<EditUserRequestDTO> returnList = new ArrayList<>();
        for (EditUserRequest eur : allRequests) {
            returnList.add(new EditUserRequestDTO(eur));
        }
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }

    @PutMapping(
            path = "/changeNotificationStatus",
            produces = "application/json"
    )public ResponseEntity<?> changeNotificationStatus(@RequestBody EditUserRequestDTO request) {
        EditUserRequest eur = editUserRequestService.changeStatus(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(
            path = "/getUser/{id}",
            produces = "application/json"
    )public ResponseEntity<UserProfile> getUser(@PathVariable Long id) throws AccessDeniedException {
        User user = userService.findById(id);
        return new ResponseEntity<>(new UserProfile(user), HttpStatus.OK);
    }

    @PostMapping(
            path = "/blockUser/{id}",
            produces = "application/json"
    ) public ResponseEntity<?> blockUser(@PathVariable Long id) throws AccessDeniedException {
        userService.blockUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(
            path = "/unblockUser/{id}",
            produces = "application/json"
    ) public ResponseEntity<?> unblockUser(@PathVariable Long id) throws AccessDeniedException {
        userService.unblockUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
