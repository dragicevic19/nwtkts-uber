package com.nwtkts.uber.dto;

import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.Ride;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveDriverDTO {
    private Long id;
    private boolean available;
    private Long vehicleId;
    private String usersRoute;
    private double driversLatitude;
    private double driversLongitude;
    private double pickupLocationLatitude;
    private double pickupLocationLongitude;

    public ActiveDriverDTO(Driver driver) {
        this.id = driver.getId();
        this.available = driver.getAvailable();
        this.vehicleId = driver.getVehicle().getId();
        this.driversLatitude = driver.getVehicle().getCurrentLocation().getLatitude();
        this.driversLongitude = driver.getVehicle().getCurrentLocation().getLongitude();
    }

    public ActiveDriverDTO(Driver driver, Ride ride) {
        this.id = driver.getId();
        this.available = driver.getAvailable();
        this.vehicleId = driver.getVehicle().getId();
        this.driversLatitude = driver.getVehicle().getCurrentLocation().getLatitude();
        this.driversLongitude = driver.getVehicle().getCurrentLocation().getLongitude();
        this.pickupLocationLatitude = ride.getStartingLocation().getLatitude();
        this.pickupLocationLongitude = ride.getStartingLocation().getLongitude();
        this.usersRoute = ride.getRouteJSON();
    }

}
