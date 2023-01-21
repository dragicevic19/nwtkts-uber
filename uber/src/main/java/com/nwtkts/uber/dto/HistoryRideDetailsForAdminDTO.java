package com.nwtkts.uber.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.nwtkts.uber.model.ClientRide;
import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HistoryRideDetailsForAdminDTO {

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

    List<ClientDTO> clients;

    public HistoryRideDetailsForAdminDTO(Ride ride, List<User> clients) {
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

        this.clients = new ArrayList<>();
        for (User c : clients) {
            ClientDTO clientDTO = new ClientDTO(c);
            this.clients.add(clientDTO);
        }
    }


}
