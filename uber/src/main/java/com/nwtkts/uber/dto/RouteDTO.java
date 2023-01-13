package com.nwtkts.uber.dto;

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

}
