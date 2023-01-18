package com.nwtkts.uber.service;

import com.nwtkts.uber.dto.RideRequest;
import com.nwtkts.uber.model.Client;
import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.Ride;

import javax.transaction.Transactional;

public interface RequestRideService {

    @Transactional
    Ride makeRideRequest(Client client, RideRequest rideRequest);


    @Transactional
    Driver searchDriver(Ride ride);
}
