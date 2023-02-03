package com.nwtkts.uber.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReceivedMessageDTO {
    private Long senderId;
    private Long receiverId;
    private String text;
}
