package com.nwtkts.uber.service;

import com.nwtkts.uber.model.Driver;
import com.nwtkts.uber.model.Ride;
import org.springframework.transaction.annotation.Transactional;

public interface FindDriverService  {
    @Transactional
    Driver searchDriver(Ride ride);
}
