package com.nwtkts.uber.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// DTO koji preuzima podatke iz HTML forme za registraciju

public class RegistrationRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public RegistrationRequest(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}