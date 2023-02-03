package com.nwtkts.uber.dto;

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
public class ClientsSplitFareRideDTO {

    private Long id;
    private String requestedBy;
    private String pickup;
    private String destination;
    private Double pricePerPerson;
    private RideStatus rideStatus;
    private List<Long> clientIds;

    public ClientsSplitFareRideDTO(Ride ride) {
        this.id = ride.getId();
        this.requestedBy = ride.getRequestedBy();
        List<String> addressValues = new ArrayList<>(ride.getLocationNames().values());
        this.pickup = addressValues.get(0);
        this.destination = addressValues.get(addressValues.size() - 1);
        this.pricePerPerson = ride.getPrice() / ride.getClientsInfo().size();
        this.rideStatus = ride.getRideStatus();
        this.clientIds = new ArrayList<>();
        for(ClientRide clientRide : ride.getClientsInfo()) {
            this.clientIds.add(clientRide.getClient().getId());
        }
    }

}
