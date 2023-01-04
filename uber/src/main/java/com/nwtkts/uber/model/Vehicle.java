package com.nwtkts.uber.model;

import com.nwtkts.uber.dto.VehicleDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "type_id")
    private VehicleType type;

    @Column
    private String make;

    @Column
    private String model;

    @Column
    private String licensePlateNumber;

    @Column
    private Integer makeYear;

    @Column
    private Boolean petsAllowed;

    @Column
    private Boolean babiesAllowed;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "rating_id", nullable = false)
    private Rating rating;

    @Embedded
    private Location currentLocation;

    public Vehicle(VehicleDTO vehicleDTO) {
        this.id = vehicleDTO.getId();
        this.licensePlateNumber = vehicleDTO.getLicensePlateNumber();
        this.currentLocation = new Location(vehicleDTO.getLatitude(), vehicleDTO.getLongitude());
        this.rating = new Rating();
    }

}
