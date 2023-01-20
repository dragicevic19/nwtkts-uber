package com.nwtkts.uber.model;import javax.persistence.*;


import com.nwtkts.uber.dto.RouteDTO;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "json", typeClass = JsonType.class)
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "name", nullable = true)
    private String name;
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private String legsStr;
    @Column
    private double duration;
    @Column
    private double distance;
    @Column
    private double price;
    @Column
    private double startingLatitude;
    @Column
    private double startingLongitude;
    @Column
    private double endingLatitude;
    @Column
    private double endingLongitude;

    public Route(RouteDTO routeDTO) {
        this. name = "Route";
        this.legsStr = routeDTO.getLegsStr();
        this.duration = routeDTO.getDuration();
        this.distance = routeDTO.getDistance();
        this.price = routeDTO.getPrice();
        this.startingLatitude = routeDTO.getStartingLatitude();
        this.startingLongitude = routeDTO.getStartingLongitude();
        this.endingLatitude = routeDTO.getEndingLatitude();
        this.endingLongitude = routeDTO.getEndingLongitude();
    }
}
