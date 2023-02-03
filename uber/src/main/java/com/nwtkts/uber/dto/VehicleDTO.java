package com.nwtkts.uber.dto;

import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
    private Long id;
    private String licensePlateNumber;
    private double latitude;
    private double longitude;
    private boolean available;

    public VehicleDTO(Vehicle vehicle, Driver driver) {
        this.id = vehicle.getId();
        this.licensePlateNumber = vehicle.getLicensePlateNumber();
        this.latitude = vehicle.getCurrentLocation().getLatitude();
        this.longitude = vehicle.getCurrentLocation().getLongitude();
        this.available = driver.getAvailable();
    }
}
