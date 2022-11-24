package com.nwtkts.uber.controller;

import com.nwtkts.uber.dto.DriverRegistrationDTO;
import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.User;
import com.nwtkts.uber.service.DriverService;
import com.nwtkts.uber.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private DriverService driverService;


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
}
