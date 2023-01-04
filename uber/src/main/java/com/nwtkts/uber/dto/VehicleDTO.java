package com.nwtkts.uber.dto;

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

    public VehicleDTO(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.licensePlateNumber = vehicle.getLicensePlateNumber();
        this.latitude = vehicle.getCurrentLocation().getLatitude();
        this.longitude = vehicle.getCurrentLocation().getLongitude();
    }
}
