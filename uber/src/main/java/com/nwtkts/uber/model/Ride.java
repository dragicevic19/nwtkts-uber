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
import java.util.*;

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
    private LocalDateTime scheduledFor;
    @Column
    private Double calculatedDuration;
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
    private Set<ClientRide> clientsInfo;

    @Column
    private boolean petsOnRide;
    @Column
    private boolean babiesOnRide;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
    private VehicleType requestedVehicleType;

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

    @ElementCollection
    @MapKeyColumn(name = "address_position")
    @Column
    private Map<Long, String> locationNames;

    @Column
    private String requestedBy;


    public Ride(RideDTO rideDTO) {
        this.id = rideDTO.getId();
        this.routeJSON = rideDTO.getRouteJSON();
        this.rideStatus = rideDTO.getRideStatus();
        this.clientsInfo = new HashSet<>();
    }

    public Ride(RideRequest rideRequest, double PRICE_PER_KM, VehicleType requestedVehicleType) {
        double price = rideRequest.getSelectedRoute().getDistance() * PRICE_PER_KM;
        price += rideRequest.getVehicleType().getAdditionalPrice();
        this.clientsInfo = new HashSet<>();
        this.price = price;
        this.scheduledFor = (rideRequest.getScheduled() == null) ? null : calcScheduledDateTime(rideRequest.getScheduled());
        this.calculatedDuration= rideRequest.getSelectedRoute().getDuration();
        this.rideStatus = RideStatus.WAITING_FOR_PAYMENT;
        this.routeJSON = rideRequest.getSelectedRoute().getLegsStr();
        this.babiesOnRide = rideRequest.isBabies();
        this.petsOnRide = rideRequest.isPets();
        this.requestedVehicleType = requestedVehicleType;
        long key = 0;
        this.locationNames = new TreeMap<>();
        for (String addressStr: rideRequest.getAddressValuesStr()) {
            this.locationNames.put(key++, addressStr);
        }
        this.startingLocation =
                new Location(rideRequest.getSelectedRoute().getStartingLatitude(), rideRequest.getSelectedRoute().getStartingLongitude());
        this.endingLocation =
                new Location(rideRequest.getSelectedRoute().getEndingLatitude(), rideRequest.getSelectedRoute().getEndingLongitude());
    }

    private LocalDateTime calcScheduledDateTime(String scheduled) {
        LocalDateTime scheduledDateTime = LocalDateTime.now();
        String[] hoursMinutes = scheduled.split(":");
        int hours = Integer.parseInt(hoursMinutes[0]);
        int minutes = Integer.parseInt(hoursMinutes[1]);

        int addHours = (hours < scheduledDateTime.getHour()) ?
                (24 - scheduledDateTime.getHour()) + hours : hours - scheduledDateTime.getHour();

        scheduledDateTime = scheduledDateTime.plusHours(addHours);

        int addMinutes = minutes - scheduledDateTime.getMinute();

        scheduledDateTime = scheduledDateTime.plusMinutes(addMinutes);
        return scheduledDateTime;
    }
}
