package com.nwtkts.uber.dto;

import com.nwtkts.uber.model.Client;
import com.nwtkts.uber.model.ClientRide;
import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriversRidesDTO {
    private long id;
    private RideStatus rideStatus;
    private String pickup;
    private String destination;
    private String clientImg;
    private String client;

    public DriversRidesDTO(Ride ride) {
        this.id = ride.getId();
        this.rideStatus = ride.getRideStatus();
        String[] locationStrings = new String[ ride.getLocationNames().size() ];
        locationStrings = ride.getLocationNames().toArray(locationStrings);
        this.pickup = locationStrings[0];
        this.destination = locationStrings[ride.getLocationNames().size() - 1];
    }
}
