package com.nwtkts.uber.service;

import com.nwtkts.uber.dto.ClientRegistrationRequest;
import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.User;

public interface DriverService {
    Driver save(User u, ClientRegistrationRequest userRequest);
}
