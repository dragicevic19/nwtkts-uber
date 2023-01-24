package com.nwtkts.uber.dto;

import com.nwtkts.uber.model.Route;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDTO {
    private String legsStr;
    private double duration;
    private double distance;
    private double price;

    private double startingLatitude;
    private double startingLongitude;
    private double endingLatitude;
    private double endingLongitude;

    public RouteDTO(Route favRoute) {
        this.legsStr = favRoute.getLegsStr();
        this.duration = favRoute.getDuration();
        this.distance = favRoute.getDistance();
        this.price = favRoute.getPrice();
        this.startingLatitude = favRoute.getStartingLatitude();
        this.startingLongitude = favRoute.getStartingLongitude();
        this.endingLatitude = favRoute.getEndingLatitude();
        this.endingLongitude = favRoute.getEndingLongitude();
    }
}
