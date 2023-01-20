package com.nwtkts.uber.dto;

import com.nwtkts.uber.model.Client;
import com.nwtkts.uber.model.ClientRide;
import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


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
    private Long driverId;


    public DriversRidesDTO(Ride ride) {
        this.id = ride.getId();
        this.rideStatus = ride.getRideStatus();
        this.driverId = ride.getDriver().getId();
        List<String> addressValues = new ArrayList<>(ride.getLocationNames().values());

        this.pickup = addressValues.get(0);
        this.destination = addressValues.get(addressValues.size() - 1);
    }
}
