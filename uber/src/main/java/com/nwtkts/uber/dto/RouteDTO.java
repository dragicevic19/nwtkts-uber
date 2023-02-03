package com.nwtkts.uber.dto;

import com.nwtkts.uber.model.Route;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    private List<String> addressValuesStr;


    public RouteDTO(Route route) {
        this.legsStr = route.getLegsStr();
        this.duration = route.getDuration();
        this.distance = route.getDistance();
        this.price = route.getPrice();
        this.startingLatitude = route.getStartingLatitude();
        this.startingLongitude = route.getStartingLongitude();
        this.endingLatitude = route.getEndingLatitude();
        this.endingLongitude = route.getEndingLongitude();
        this.addressValuesStr = new ArrayList<>(route.getLocationNames().values());
    }
}
