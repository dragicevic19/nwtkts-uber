package com.nwtkts.uber.dto;

import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideReqResponse {
    private RideStatus rideStatus;

    public RideReqResponse(Ride ride) {
        this.rideStatus = ride.getRideStatus();
    }
}
