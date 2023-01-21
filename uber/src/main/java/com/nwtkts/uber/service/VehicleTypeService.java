package com.nwtkts.uber.service;

import com.nwtkts.uber.model.VehicleType;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface VehicleTypeService {
    List<VehicleType> findAll() throws AccessDeniedException;
}
