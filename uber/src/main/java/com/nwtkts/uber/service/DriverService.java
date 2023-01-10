package com.nwtkts.uber.service;

import com.nwtkts.uber.dto.DriverRegistrationDTO;
import com.nwtkts.uber.model.Driver;

import java.util.List;

public interface DriverService {
    Driver register(DriverRegistrationDTO driverRegistrationDTO);

    List<Driver> getDriversByActivity(boolean active);

    Driver findById(Long id);

    void setActiveDriver(Driver driver);
}
