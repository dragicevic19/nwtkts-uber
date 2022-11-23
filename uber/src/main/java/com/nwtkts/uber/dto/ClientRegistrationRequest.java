package com.nwtkts.uber.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// DTO koji preuzima podatke iz HTML forme za registraciju

public class ClientRegistrationRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String country;
    private String city;
    private String street;
    private String phoneNumber;

}