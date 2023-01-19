package com.nwtkts.uber.dto;

import com.nwtkts.uber.model.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequest {
    private RouteDTO selectedRoute;
    private VehicleType vehicleType;
    private boolean pets;
    private boolean babies;
    private List<UserProfile> addedFriends;
    private double price;
    private double pricePerPerson;
    private String scheduled;
    private List<String> addressValuesStr;

}
