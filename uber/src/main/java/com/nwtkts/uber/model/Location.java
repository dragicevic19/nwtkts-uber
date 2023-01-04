package com.nwtkts.uber.model;

import lombok.*;

import javax.persistence.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Column
    private Double latitude;
    @Column
    private Double longitude;
}
