package com.nwtkts.uber.dto;

import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideDTO {
    private Long id;
    private String routeJSON;
    private RideStatus rideStatus;
    private VehicleDTO vehicle;

    public RideDTO(Ride ride) {
        this.id = ride.getId();
        this.routeJSON = ride.getRouteJSON();
        this.rideStatus = ride.getRideStatus();
        this.vehicle = new VehicleDTO(ride.getVehicle());
    }
}
