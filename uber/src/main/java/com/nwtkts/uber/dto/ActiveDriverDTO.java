package com.nwtkts.uber.dto;

import com.nwtkts.uber.model.Driver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveDriverDTO {
    private Long id;
    private boolean isAvailable;
    private Long vehicleId;

    public ActiveDriverDTO(Driver driver) {
        this.id = driver.getId();
        this.isAvailable = driver.getAvailable();
        this.vehicleId = driver.getVehicle().getId();
    }

}
