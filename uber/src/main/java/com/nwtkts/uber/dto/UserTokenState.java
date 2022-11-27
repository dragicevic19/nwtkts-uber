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
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String type;

    public UserTokenState() {
        this.accessToken = null;
        this.expiresIn = null;
    }

    public UserTokenState(String accessToken, long expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

    public UserTokenState(String accessToken, long expiresIn, User user) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.type = user.getRoles().get(0).getName(); // ?
    }
}