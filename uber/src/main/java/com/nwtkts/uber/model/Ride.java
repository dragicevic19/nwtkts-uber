package com.nwtkts.uber.model;

import com.nwtkts.uber.dto.RideDTO;
import com.nwtkts.uber.dto.RideRequest;
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
    private boolean petsOnRide;
    @Column
    private boolean babiesOnRide;

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

    public Ride(RideDTO rideDTO) {
        this.id = rideDTO.getId();
        this.routeJSON = rideDTO.getRouteJSON();
        this.rideStatus = rideDTO.getRideStatus();
        this.clientsInfo = new ArrayList<>();
    }

    public Ride(RideRequest rideRequest, double PRICE_PER_KM) {
        double price = rideRequest.getSelectedRoute().getDistance() * PRICE_PER_KM;
        price += rideRequest.getVehicleType().getAdditionalPrice();

        this.setPrice(price);
        this.setScheduled(rideRequest.isScheduled());
        this.setCalculatedDuration(rideRequest.getSelectedRoute().getDuration());
        this.setRideStatus(RideStatus.WAITING);
        this.setRouteJSON(rideRequest.getSelectedRoute().getLegsStr());
        this.setBabiesOnRide(rideRequest.isBabies());
        this.setPetsOnRide(rideRequest.isPets());

        this.setStartingLocation(
                new Location(rideRequest.getSelectedRoute().getStartingLatitude(), rideRequest.getSelectedRoute().getStartingLongitude()));
        this.setEndingLocation(
                new Location(rideRequest.getSelectedRoute().getEndingLatitude(), rideRequest.getSelectedRoute().getEndingLongitude()));
    }
}
