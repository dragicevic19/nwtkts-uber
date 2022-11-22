package com.nwtkts.uber.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//DTO za login
public class JwtAuthenticationRequest {

    private String email;
    private String password;

}