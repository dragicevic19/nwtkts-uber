package com.nwtkts.uber.dto;


import com.nwtkts.uber.model.User;
import lombok.Getter;
import lombok.Setter;

// DTO koji enkapsulira generisani JWT i njegovo trajanje koji se vracaju klijentu
@Getter
@Setter
public class UserTokenState {

    private String accessToken;
    private Long expiresIn;
    private Boolean fullRegDone;

    public UserTokenState() {
        this.accessToken = null;
        this.expiresIn = null;
        this.fullRegDone = null;
    }

    public UserTokenState(String accessToken, long expiresIn, Boolean fullRegDone) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.fullRegDone = fullRegDone;
    }
}