package com.nwtkts.uber.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nwtkts.uber.model.Location;
import com.nwtkts.uber.model.Ride;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HistoryRideDTO {

    private Long id;

//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    private Double calculatedDuration;
    private String routeJSON;
    private String pickup;
    private String destination;
    private Double price;

    public HistoryRideDTO(Ride ride) {
        this.id = ride.getId();
        this.price = ride.getPrice();
        this.startTime = ride.getStartTime();
        this.calculatedDuration = ride.getCalculatedDuration();
        this.routeJSON = ride.getRouteJSON();

        List<String> addressValues = new ArrayList<>(ride.getLocationNames().values());
        this.pickup = addressValues.get(0);
        this.destination = addressValues.get(addressValues.size() - 1);
    }

}
