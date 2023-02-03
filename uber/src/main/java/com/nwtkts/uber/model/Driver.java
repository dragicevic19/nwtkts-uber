package com.nwtkts.uber.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Driver extends User {



    @Column(name="active", nullable = false)
    private Boolean active;
    @Column(name="available", nullable = false)
    private Boolean available;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "rating_id", nullable = false)
    private Rating rating;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="driver_id", referencedColumnName = "id")
    private List<DriverActivity> activities;

    @Column
    private Long nextRideId;

    public Driver(User u) {
        super(u);
    }

    public Driver(User u,
                  boolean active,
                  boolean available,
                  Vehicle vehicle,
                  Rating rating,
                  List<DriverActivity> driverActivities,
                  long nextRide) {
        super(u);
        this.active = active;
        this.available = available;
        this.vehicle = vehicle;
        this.rating = rating;
        this.activities = driverActivities;
        this.nextRideId = nextRide;
    }
}
