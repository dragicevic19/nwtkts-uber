package com.nwtkts.uber.dto;

import com.nwtkts.uber.model.Location;
import com.nwtkts.uber.model.Ride;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HistoryRideDTO {

    private Long id;
    private LocalDateTime startTime;
    private Integer calculatedDuration;
    private String routeJSON;
    private Double price;

    public HistoryRideDTO(Ride ride) {
        this.id = ride.getId();
        this.price = ride.getPrice();
        this.startTime = ride.getStartTime();
        this.calculatedDuration = ride.getCalculatedDuration();
        this.routeJSON = ride.getRouteJSON();
    }

}
