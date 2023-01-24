package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.model.Route;
import com.nwtkts.uber.repository.RouteRepository;
import com.nwtkts.uber.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteServiceImpl implements RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Override
    public Route findDetailedRouteById(Long id) {
        return routeRepository.findDetailedById(id);
    }
}
