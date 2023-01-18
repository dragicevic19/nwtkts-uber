package com.nwtkts.uber.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RideRatingDTO {

    private Long rideId;
    private Integer driverRating;
    private Integer vehicleRating;

}
