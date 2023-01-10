package com.nwtkts.uber.controller;


import com.nwtkts.uber.dto.LocationDTO;
import com.nwtkts.uber.dto.VehicleDTO;
import com.nwtkts.uber.model.Vehicle;
import com.nwtkts.uber.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public VehicleController(VehicleService vehicleService, SimpMessagingTemplate simpMessagingTemplate) {
        this.vehicleService = vehicleService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PostMapping(
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<VehicleDTO> createVehicle(@RequestBody VehicleDTO vehicleDTO) {
        Vehicle vehicle = this.vehicleService.createVehicle(new Vehicle(vehicleDTO));
        VehicleDTO returnVehicleDTO = new VehicleDTO(vehicle);
        return new ResponseEntity<>(returnVehicleDTO, HttpStatus.OK);
    }

    @PutMapping(
            path = "/{id}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<VehicleDTO> updateVehicleLocation(@PathVariable("id") Long id, @RequestBody LocationDTO locationDTO) {
        Vehicle vehicle = this.vehicleService.updateVehicleLocation(id, locationDTO.getLatitude(), locationDTO.getLongitude());
        VehicleDTO returnVehicleDTO = new VehicleDTO(vehicle);
        this.simpMessagingTemplate.convertAndSend("/map-updates/update-vehicle-position", returnVehicleDTO);
        return new ResponseEntity<>(returnVehicleDTO, HttpStatus.OK);
    }

    @DeleteMapping(
            produces = "text/plain"
    )
    public ResponseEntity<String> deleteAllVehicles() {
        this.vehicleService.deleteAllVehicles();
        return new ResponseEntity<>("All vehicles deleted!", HttpStatus.OK);
    }
}