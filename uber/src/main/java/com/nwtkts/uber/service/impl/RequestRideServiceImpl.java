package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.dto.RideRequest;
import com.nwtkts.uber.dto.UserProfile;
import com.nwtkts.uber.exception.NotFoundException;
import com.nwtkts.uber.model.*;
import com.nwtkts.uber.repository.ClientRepository;
import com.nwtkts.uber.repository.DriverRepository;
import com.nwtkts.uber.repository.RideRepository;
import com.nwtkts.uber.repository.VehicleTypeRepository;
import com.nwtkts.uber.service.ClientService;
import com.nwtkts.uber.service.FindDriverService;
import com.nwtkts.uber.service.RequestRideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class RequestRideServiceImpl implements RequestRideService {
    private static final double PRICE_PER_KM = 1.3;

    @Autowired
    private ClientService clientService;
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;
    @Autowired
    private FindDriverService findDriverService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Ride makeRideRequest(Client client, RideRequest rideRequest) {
        VehicleType requestedVehicleType = vehicleTypeRepository.findById(rideRequest.getVehicleType().getId())
                .orElseThrow(() -> new NotFoundException("Vehicle type doesn't exist!"));

        Ride newRide = new Ride(rideRequest, PRICE_PER_KM, requestedVehicleType);

        double pricePerPerson = (double) Math.round((newRide.getPrice() / (rideRequest.getAddedFriends().size() + 1)) * 100)/100;
        this.clientService.makePayment(client, pricePerPerson);

        newRide.setClientsInfo(makeClientsInfos(client, rideRequest));
        newRide.setRequestedBy(client.getEmail());
        newRide = this.rideRepository.save(newRide);

        return afterClientPays(client, newRide);
    }

    @Override
    @Transactional
    public Ride afterClientPays(Client client, Ride newRide) {
        if (areAllClientsFinishedPayment(newRide)) {

            if (newRide.getScheduledFor() != null) {
                newRide.setRideStatus(RideStatus.SCHEDULED);
                return this.rideRepository.save(newRide);
            }

            Driver driver = this.findDriverService.searchDriver(newRide);
            if (driver != null) {
                this.driverFounded(newRide, driver);
            } else {
                newRide.setRideStatus(RideStatus.CANCELED);
                newRide.setCancellationReason("Can't find driver");
                double pricePerPerson = (double) Math.round((newRide.getPrice() / (newRide.getClientsInfo().size())) * 100)/100;

                this.clientService.refundForCanceledRide(client, pricePerPerson);
            }
        }
        return this.rideRepository.save(newRide);
    }


    private void driverFounded(Ride newRide, Driver driver) {
        if (driver.getNextRideId() == null) {   // nasao je odmah slobodnog vozaca
            driver.setAvailable(false);
            newRide.setRideStatus(RideStatus.TO_PICKUP);
        }
        else {
            newRide.setRideStatus(RideStatus.WAITING_FOR_DRIVER_TO_FINISH);
        }
        newRide.setDriver(driver);
        newRide.setVehicle(driver.getVehicle());
        this.driverRepository.save(driver);
    }


    private boolean areAllClientsFinishedPayment(Ride ride) {
        for (ClientRide clientRide : ride.getClientsInfo()) {
            if (!clientRide.isClientPaid()) return false;
        }
        return true;
    }

    private Set<ClientRide> makeClientsInfos(Client client, RideRequest rideRequest) {
        Set<ClientRide> clientsInfo = new HashSet<>();

        clientsInfo.add(new ClientRide(client, true));

        for (UserProfile user : rideRequest.getAddedFriends()) {
            Client c = clientRepository.findById(user.getId()).orElseThrow(() -> new NotFoundException("Added friend doesn't exist"));
            clientsInfo.add(new ClientRide(c));
        }
        return clientsInfo;
    }
}
