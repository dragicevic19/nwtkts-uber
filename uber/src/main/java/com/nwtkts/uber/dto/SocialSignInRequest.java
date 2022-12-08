package com.nwtkts.uber.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SocialSignInRequest extends RegistrationRequest {
    private String picture;

    public SocialSignInRequest(String givenName, String familyName, String email, String pictureUrl) {
        super(givenName, familyName, email);
        this.picture = pictureUrl;
    }
}
