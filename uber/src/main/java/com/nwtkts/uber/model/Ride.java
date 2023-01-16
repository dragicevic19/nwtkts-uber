package com.nwtkts.uber.model;

import com.nwtkts.uber.dto.RideDTO;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private double calculatedDuration;
    @Enumerated(EnumType.STRING)
    @Column
    private RideStatus rideStatus;
    @Column
    private String cancellationReason;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private String routeJSON;

    @ManyToOne(fetch = FetchType.EAGER)
    private Vehicle vehicle;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id")
    private Driver driver;


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ride_id", referencedColumnName = "id")
    private List<ClientRide> clientsInfo;

    @Column
    private boolean petsAllowed;
    @Column
    private boolean babiesAllowed;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "starting_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "starting_longitude"))
    })
    private Location startingLocation;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "ending_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "ending_longitude"))
    })
    private Location endingLocation;

    //    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    //    @JoinColumn(name = "route_id", nullable = false)
    //    private Route route;
    //
    //    @ElementCollection()
    //    private List<Location> destinations;
    public Ride(RideDTO rideDTO) {
        this.id = rideDTO.getId();
        this.routeJSON = rideDTO.getRouteJSON();
        this.rideStatus = rideDTO.getRideStatus();
        this.clientsInfo = new ArrayList<>();
    }
}
