package com.nwtkts.uber.dto;

import com.nwtkts.uber.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    private Long id;
    private String firstName;
    private String lastName;
    private String image;
    private String email;
    private String phone;
    private String country;
    private String city;
    private String street;
    private String role;
    private boolean hasPassword;


    public UserProfile(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.image = user.getPhoto();
        this.email = user.getEmail();
        this.phone = user.getPhoneNumber();
        this.country = user.getAddress().getCountry();
        this.city = user.getAddress().getCity();
        this.street = user.getAddress().getStreet();
        this.role = user.getRoles().get(0).getName();
        this.hasPassword = user.getPassword() != null;
    }
}
