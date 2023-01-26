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
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveRideDTO {
    private long id;
    private RideStatus rideStatus;
    private String pickup;
    private String destination;
    private String clientImg;
    private String client;
    private Long driverId;
    private List<Long> clientIds = new ArrayList<>();


    public ActiveRideDTO(Ride ride) {
        this.id = ride.getId();
        this.rideStatus = ride.getRideStatus();
        if (ride.getDriver() != null) {
            this.driverId = ride.getDriver().getId();
        }
        List<String> addressValues = new ArrayList<>(ride.getLocationNames().values());

        this.pickup = addressValues.get(0);
        this.destination = addressValues.get(addressValues.size() - 1);
    }

    public ActiveRideDTO(Ride ride, Long driverId) {
        this(ride);
        this.driverId = driverId;
    }

    public ActiveRideDTO(Ride ride, Set<ClientRide> clientRides) {
        this(ride);
        for (ClientRide clientRide: clientRides) {
            this.clientIds.add(clientRide.getClient().getId());
        }
    }

    public ActiveRideDTO(Ride ride, Long driverId, Set<ClientRide> clientRides) {
        this(ride, driverId);
        for (ClientRide clientRide: clientRides) {
            this.clientIds.add(clientRide.getClient().getId());
        }
    }
}
