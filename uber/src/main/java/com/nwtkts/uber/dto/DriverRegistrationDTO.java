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
    private String license_plate_number;
    private Integer makeYear;
    private Boolean petsAllowed;
    private Boolean babiesAllowed;
    // fali u RegistrationReguest ovi podaci, dodacemo posle
    private String phone_number;
    private String country;
    private String street;
    private String city;


}
