package com.nwtkts.uber.dto;

import com.nwtkts.uber.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatListItemDTO {
    private Long id;
    private String name;
    private String img;

    public ChatListItemDTO(User user) {
        this.id = user.getId();
        this.name = user.getFirstName() + " " + user.getLastName();
        this.img = user.getPhoto();
    }
}
