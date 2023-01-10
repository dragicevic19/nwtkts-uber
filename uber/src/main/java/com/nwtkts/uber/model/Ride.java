package com.nwtkts.uber.model;

import com.nwtkts.uber.dto.FakeRideDTO;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "json", typeClass = JsonType.class)
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column
    private Double price;
    @Column
    private LocalDateTime startTime;
    @Column
    private Boolean scheduled;
    @Column
    private Integer calculatedDuration;
    @Enumerated(EnumType.STRING)
    @Column
    private RideStatus rideStatus;
    @Column
    private String cancellationReason;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private String routeJSON;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Vehicle Vehicle;

    @Embedded
    private Location startingLocation;

    @ElementCollection
    private List<Location> destinations;

//    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "driver_id", nullable = false)
//    private Driver driver;

//    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "route_id", nullable = false)
//    private Route route;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="ride_id", referencedColumnName = "id")
    private List<ClientRide> clientsInfo;


    public Ride(FakeRideDTO rideDTO) {
        this.id = rideDTO.getId();
        this.routeJSON = rideDTO.getRouteJSON();
        this.rideStatus = rideDTO.getRideStatus();
    }
}
