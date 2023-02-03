package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.model.VehicleType;
import com.nwtkts.uber.repository.VehicleTypeRepository;
import com.nwtkts.uber.service.VehicleTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleTypeServiceImpl implements VehicleTypeService {
    private final VehicleTypeRepository vehicleTypeRepository;

    public VehicleTypeServiceImpl(VehicleTypeRepository vehicleTypeRepository) {
        this.vehicleTypeRepository = vehicleTypeRepository;
    }

    public List<VehicleType> findAll() {
        return vehicleTypeRepository.findAll();
    }
}
