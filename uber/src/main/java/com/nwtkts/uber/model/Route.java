package com.nwtkts.uber.model;import javax.persistence.*;


import com.nwtkts.uber.dto.FavRouteDTO;
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
import java.util.Map;
import java.util.TreeMap;

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

    @Column
    private String pickup;
    @Column
    private String destination;

    @ElementCollection
    @MapKeyColumn(name = "address_position")
    @Column
    private Map<Long, String> locationNames;

    public Route(FavRouteDTO routeDTO) {
        this.name = "Route";
        this.legsStr = routeDTO.getSelectedRoute().getLegsStr();
        this.duration = routeDTO.getSelectedRoute().getDuration();
        this.distance = routeDTO.getSelectedRoute().getDistance();
        this.price = routeDTO.getSelectedRoute().getPrice();
        this.startingLatitude = routeDTO.getSelectedRoute().getStartingLatitude();
        this.startingLongitude = routeDTO.getSelectedRoute().getStartingLongitude();
        this.endingLatitude = routeDTO.getSelectedRoute().getEndingLatitude();
        this.endingLongitude = routeDTO.getSelectedRoute().getEndingLongitude();
        this.pickup = routeDTO.getPickup();
        this.destination = routeDTO.getDestination();
        this.locationNames = new TreeMap<>();
        long key = 0;
        for (String addressStr: routeDTO.getSelectedRoute().getAddressValuesStr()) {
            this.locationNames.put(key++, addressStr);
        }
    }
}
