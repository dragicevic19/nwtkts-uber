package com.nwtkts.uber.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RideCancelationDTO {

    private Long rideId;
    private String cancelationReason;


}
