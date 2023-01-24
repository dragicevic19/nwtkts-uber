package com.nwtkts.uber.service;

import com.nwtkts.uber.model.Route;

public interface RouteService {
    Route findDetailedRouteById(Long id);
}
