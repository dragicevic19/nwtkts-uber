package com.nwtkts.uber.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.nwtkts.uber.model.ClientRide;
import com.nwtkts.uber.model.Ride;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HistoryRideDetailsDTO {

    private Long id;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    private Double calculatedDuration;
    private String routeJSON;
    private String pickup;
    private String destination;
    private Double price;

    private String driverEmail;
    private String driverFirstName;
    private String driverLastName;
    private String licencePlateNumber;


    private Boolean driverRated;
    private Boolean vehicleRated;
    private Integer driverRating;
    private Integer vehicleRating;

    public HistoryRideDetailsDTO(Ride ride, ClientRide clientRide) {
        this.id = ride.getId();
        this.price = ride.getPrice();
        this.startTime = ride.getStartTime();
        this.calculatedDuration = ride.getCalculatedDuration();
        this.routeJSON = ride.getRouteJSON();

        List<String> addressValues = new ArrayList<>(ride.getLocationNames().values());
        this.pickup = addressValues.get(0);
        this.destination = addressValues.get(addressValues.size() - 1);

        this.driverEmail = ride.getDriver().getEmail();
        this.driverFirstName = ride.getDriver().getFirstName();
        this.driverLastName = ride.getDriver().getLastName();
        this.licencePlateNumber = ride.getVehicle().getLicensePlateNumber();

        this.driverRated = clientRide.getDriverRated();
        this.vehicleRated = clientRide.getDriverRated();
        this.driverRating = clientRide.getDriverRating();
        this.vehicleRating = clientRide.getVehicleRating();


    }

}
