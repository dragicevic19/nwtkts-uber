package com.nwtkts.uber.service;

import com.nwtkts.uber.dto.DriverRegistrationDTO;
import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.User;
import com.nwtkts.uber.model.Vehicle;

import java.util.List;

public interface DriverService {
    Driver register(DriverRegistrationDTO driverRegistrationDTO);

    List<Driver> getDriversByActivity(boolean active);

    Driver findById(Long id);

    void setActiveDriver(Driver driver);

    Driver getDriverForVehicle(Vehicle vehicle);

    void driverLoggedIn(Driver loggedInUser);

    void activateIfUserIsDriver(User user);

    Driver findDetailedByEmail(String name);
}
