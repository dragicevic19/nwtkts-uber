package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.dto.ClientRegistrationRequest;
import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.User;
import com.nwtkts.uber.service.DriverService;
import org.springframework.stereotype.Service;

@Service
public class DriverServiceImpl implements DriverService {
    @Override
    public Driver save(User u, ClientRegistrationRequest userRequest) {
        Driver d = new Driver(u);

//        d.setActive(false);
//        d.setActivities(new ArrayList<>());
//        d.setAvailable(false);

        return d;
    }
}
