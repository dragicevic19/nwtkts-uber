package com.nwtkts.uber.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientRide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column
    private Boolean driverRated;
    @Column
    private Boolean vehicleRated;
    @Column
    private Integer driverRating;
    @Column
    private Integer vehicleRating;
    @Column
    private boolean clientPaid;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public ClientRide(Client client) {
        this.client = client;
        this.driverRated = false;
        this.vehicleRated = false;
        this.driverRating = 0;
        this.vehicleRating = 0;
        this.clientPaid = false;
    }

    public ClientRide(Client client, boolean clientPaid) {
        this.client = client;
        this.driverRated = false;
        this.vehicleRated = false;
        this.driverRating = 0;
        this.vehicleRating = 0;
        this.clientPaid = clientPaid;
    }
}
