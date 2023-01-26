package com.nwtkts.uber.dto;

import com.nwtkts.uber.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageSender {
    private Long id;
    private String firstName;
    private String lastName;
    private String image;

    public MessageSender(User sender) {
        this.id = sender.getId();
        this.firstName = sender.getFirstName();
        this.lastName = sender.getLastName();
        this.image = sender.getPhoto();
    }
}
