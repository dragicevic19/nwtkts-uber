package com.nwtkts.uber.service;

import com.nwtkts.uber.dto.RideRequest;
import com.nwtkts.uber.model.Client;
import com.nwtkts.uber.model.Ride;

public interface RequestRideService {


    Ride makeRideRequest(Client client, RideRequest rideRequest);
}
