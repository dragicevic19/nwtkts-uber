package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.dto.DriverRegistrationDTO;
import com.nwtkts.uber.exception.BadRequestException;
import com.nwtkts.uber.exception.NotFoundException;
import com.nwtkts.uber.model.*;
import com.nwtkts.uber.repository.DriverRepository;
import com.nwtkts.uber.repository.VehicleTypeRepository;
import com.nwtkts.uber.service.DriverService;
import com.nwtkts.uber.service.RideService;
import com.nwtkts.uber.service.RoleService;
import com.nwtkts.uber.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;
    @Autowired
    private RideService rideService;

    @Override
    public Driver register(DriverRegistrationDTO userRequest) {
        Driver d = new Driver();
        d = (Driver) userService.register(d, userRequest);
        List<Role> roles = roleService.findByName("ROLE_DRIVER");
        d.setRoles(roles);
        d.setEnabled(true);
        d.setPhoneNumber(userRequest.getPhone_number());
        d.setAddress(new Address(userRequest.getStreet(), userRequest.getCity(), userRequest.getCountry()));
        d.setAvailable(false);
        d.setActive(false);
        d.setActivities(new ArrayList<>());
        d.setRating(new Rating());
        d.setVehicle(makeVehicleFromDTO(userRequest));
        Driver savedDriver = driverRepository.save(d);
        return savedDriver;
    }

    @Override
    public void activateIfUserIsDriver(User user) {
        Driver driver = this.driverRepository.findDetailedByEmail(user.getEmail());
        if (driver != null) {
            this.driverLoggedIn(driver);
        }
    }

    @Override
    public Driver findDetailedByEmail(String email) {
        return this.driverRepository.findDetailedByEmail(email);
    }

    @Override
    public Driver findByEmail(String email) {
        return this.driverRepository.findSummaryByEmail(email);
    }

    @Override
    public Driver changeActive(Driver driver, Boolean active) {
        if (active) {
            driver.setActive(true);
            driver.setAvailable(true);
        } else {
            if(this.rideService.getActiveRidesForDriver(driver.getId()).size() > 0)
                throw new BadRequestException("You have to finish active rides before you change active status");
            driver.setAvailable(false);
            driver.setActive(false);
        }
        return this.driverRepository.save(driver);
    }

    @Override
    public void driverLoggedIn(Driver loggedInUser) {
        loggedInUser.setActive(true);
        loggedInUser.setAvailable(true);
        DriverActivity driverActivity = new DriverActivity(LocalDateTime.now());
        loggedInUser.getActivities().add(driverActivity);
        this.driverRepository.save(loggedInUser);
    }


    @Override
    public List<Driver> getDriversByActivity(boolean active) {
        return this.driverRepository.findAllByActive(active);
    }

    @Override
    public Driver findById(Long id) {
        return this.driverRepository.findById(id).orElseThrow(() -> new NotFoundException("Driver does not exist!"));
    }

    @Override
    public void setActiveDriver(Driver driver) {
        driver.setActive(!driver.getActive());
        driver.setAvailable(!driver.getAvailable());
        this.driverRepository.save(driver);
    }

    @Override
    public Driver getDriverForVehicle(Vehicle vehicle) {
        Driver driver = this.driverRepository.findByVehicle_Id(vehicle.getId());
//        for (Driver driver : this.driverRepository.findAll()) {
//            if (driver.getVehicle().getId() == vehicle.getId()) return driver;
//        }
        return driver;
    }


    private Vehicle makeVehicleFromDTO(DriverRegistrationDTO userRequest) {
        Vehicle v = new Vehicle();
        v.setMake(userRequest.getVehicleMake());
        Location loc = new Location(45.24563178845125, 19.849838982677664);
        v.setCurrentLocation(loc);
        v.setRating(new Rating());
        v.setModel(userRequest.getVehicleModel());
        v.setMakeYear(userRequest.getMakeYear());
        v.setLicensePlateNumber(userRequest.getLicense_plate_number());
        v.setType(vehicleTypeRepository.findById(userRequest.getVehicleTypeId()).orElseGet(null));
        v.setBabiesAllowed(userRequest.getBabiesAllowed());
        v.setPetsAllowed(userRequest.getPetsAllowed());
        return v;
    }
}
