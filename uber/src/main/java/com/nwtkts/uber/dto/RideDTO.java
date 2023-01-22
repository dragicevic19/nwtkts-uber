package com.nwtkts.uber.dto;

import com.nwtkts.uber.model.ClientRide;
import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideDTO {
    private Long id;
    private String routeJSON;
    private RideStatus rideStatus;
    private Long driverId;
    private VehicleDTO vehicle;

    private double vehicleLatitude; // kada pravim laznu (vozac ceka na voznju)
    private double vehicleLongitude;
    private List<Long> clientIds;

    public RideDTO(Ride ride) {
        this.id = ride.getId();
        this.driverId = (ride.getDriver() != null) ? ride.getDriver().getId() : null;
        this.routeJSON = ride.getRouteJSON();
        this.rideStatus = ride.getRideStatus();
        if (ride.getVehicle() != null)
            this.vehicle = new VehicleDTO(ride.getVehicle(), ride.getDriver());

        this.clientIds = new ArrayList<>();
    }

    public RideDTO(Ride ride, Set<ClientRide> clientsInRide) {
        this(ride);
        for(ClientRide clientRide : clientsInRide) {
            this.clientIds.add(clientRide.getClient().getId());
        }
    }
}
