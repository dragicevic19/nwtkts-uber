package com.nwtkts.uber.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nwtkts.uber.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private UserProfile sender;
    private UserProfile receiver;
    private String text;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime;

    public MessageDTO(Message message) {
        this.sender = new UserProfile(message.getSender());
        this.receiver = (message.getReceiver() != null) ? new UserProfile(message.getReceiver()) : null;
        this.text = message.getText();
        this.dateTime = message.getDateTime();
    }
}
