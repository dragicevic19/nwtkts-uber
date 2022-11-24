package com.nwtkts.uber.service;

import com.nwtkts.uber.dto.DriverRegistrationDTO;
import com.nwtkts.uber.model.Driver;

public interface DriverService {
    Driver register(DriverRegistrationDTO driverRegistrationDTO);
}
