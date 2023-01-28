package com.nwtkts.uber.service;

import com.nwtkts.uber.dto.RideRequest;
import com.nwtkts.uber.model.Client;
import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.Ride;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface RequestRideService {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Ride makeRideRequest(Client client, RideRequest rideRequest);


    Ride afterClientPays(Client client, Ride newRide);
}
