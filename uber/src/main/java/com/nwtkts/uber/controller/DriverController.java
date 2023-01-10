package com.nwtkts.uber.controller;

import com.nwtkts.uber.dto.ActiveDriverDTO;
import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/driver", produces = MediaType.APPLICATION_JSON_VALUE)
public class DriverController {

    private final DriverService driverService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public DriverController(DriverService driverService, SimpMessagingTemplate simpMessagingTemplate) {
        this.driverService = driverService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping(
            path = "/active",
            produces = "application/json"
    )
    public ResponseEntity<List<ActiveDriverDTO>> getActiveDrivers() {
        List<Driver> drivers = this.driverService.getDriversByActivity(true);
        List<ActiveDriverDTO> driverDTOS = new ArrayList<>();
        for (Driver driver: drivers) {
            driverDTOS.add(new ActiveDriverDTO(driver));
        }
        return new ResponseEntity<>(driverDTOS, HttpStatus.OK);
    }

    @GetMapping(
            path = "/activate/{id}",
            produces = "application/json"
    )
    public ResponseEntity<Boolean> setDriverActive(@PathVariable("id") Long id){
        Driver driver = this.driverService.findById(id);
        this.driverService.setActiveDriver(driver);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
