package com.nwtkts.uber.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DriverRegistrationDTO extends RegistrationRequest {

    private Long vehicleTypeId;
    private String vehicleMake;
    private String vehicleModel;
    private Integer makeYear;
    private Boolean petsAllowed;
    private Boolean babiesAllowed;

}
