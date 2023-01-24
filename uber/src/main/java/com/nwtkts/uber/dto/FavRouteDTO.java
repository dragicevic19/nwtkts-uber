package com.nwtkts.uber.dto;

import com.nwtkts.uber.model.Route;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavRouteDTO {
    private Long id;
    private String name;
    private RouteDTO selectedRoute;
    private String pickup;
    private String destination;

    public FavRouteDTO(Route favRoute) {
        this.id = favRoute.getId();
        this.name = favRoute.getName();
        this.selectedRoute = new RouteDTO(favRoute);
        this.pickup = favRoute.getPickup();
        this.destination = favRoute.getDestination();
    }
}
