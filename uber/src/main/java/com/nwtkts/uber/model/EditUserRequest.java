package com.nwtkts.uber.model;

import com.nwtkts.uber.dto.EditUserRequestDTO;
import com.nwtkts.uber.dto.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditUserRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String status;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String street;

    @Column
    private String city;

    @Column
    private String role;

    @Column
    private String country;

    @Column
    private String image;

    @Column
    private boolean hasPassword;

    public EditUserRequest(User loggedUser, UserProfile up) {
        this.user = loggedUser;
        this.firstName = up.getFirstName();
        this.lastName = up.getLastName();
        this.phone = up.getPhone();
        this.email = up.getEmail();
        this.street = up.getStreet();
        this.city = up.getCity();
        this.role = up.getRole();
        this.country = up.getCountry();
        this.image = up.getImage();
        this.hasPassword = up.isHasPassword();
    }

    public EditUserRequest(EditUserRequestDTO editUserRequestDTO) {
        this.firstName = editUserRequestDTO.getNewInfo().getFirstName();
        this.lastName = editUserRequestDTO.getNewInfo().getLastName();
        this.phone = editUserRequestDTO.getNewInfo().getPhone();
        this.email = editUserRequestDTO.getNewInfo().getEmail();
        this.street = editUserRequestDTO.getNewInfo().getStreet();
        this.city = editUserRequestDTO.getNewInfo().getCity();
        setRole(editUserRequestDTO.getNewInfo().getRole());
        this.country = editUserRequestDTO.getNewInfo().getCountry();
        this.image = editUserRequestDTO.getNewInfo().getImage();
        this.hasPassword = editUserRequestDTO.getNewInfo().isHasPassword();
        this.status = editUserRequestDTO.getStatus();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRole(String smallRole){
        if (smallRole.equals("Driver")) {
            this.role = "ROLE_DRIVER";
        } else if (smallRole.equals("Client")) {
            this.role = "ROLE_CLIENT";
        }
        else {
            this.role = "ROLE_ADMIN";
        }
    }
}
