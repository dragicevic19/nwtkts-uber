package com.nwtkts.uber.dto;

import com.nwtkts.uber.model.EditUserRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditUserRequestDTO {
    private Long id;
    private UserProfile oldInfo;
    private UserProfile newInfo;
    private String status;

    public EditUserRequestDTO(EditUserRequest eur) {
        this.id = eur.getId();
        this.oldInfo = new UserProfile(eur.getUser());
        this.newInfo = new UserProfile(
                eur.getId(), eur.getFirstName(),
                eur.getLastName(), eur.getImage(),
                eur.getEmail(), eur.getPhone(),
                eur.getCountry(), eur.getCity(),
                eur.getStreet(),  eur.getRole(), true, false);
        this.status = eur.getStatus();
    }
}
