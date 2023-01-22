package com.nwtkts.uber.dto;


import com.nwtkts.uber.model.Client;
import com.nwtkts.uber.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientDTO {

    private Long clientId;
    private String email;
    private String firstName;
    private String lastName;

    public ClientDTO(User client) {
        this.clientId = client.getId();
        this.email = client.getEmail();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
    }

}
