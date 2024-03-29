package com.nwtkts.uber.dto;

import com.nwtkts.uber.model.ClientRide;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class NotificationDTO {
    private String notification;
    private List<Long> clientIds;

    public NotificationDTO(String notification, Set<ClientRide> clientsOnRide) {
        this.notification = notification;
        this.clientIds = new ArrayList<>();
        for (ClientRide clientRide : clientsOnRide) {
            clientIds.add(clientRide.getClient().getId());
        }
    }
}
