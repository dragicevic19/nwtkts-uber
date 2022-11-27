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
public class Client extends User {

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="client_id", referencedColumnName = "id", nullable = true)
    protected List<Route> favoriteRoutes;

    public Client(User u) {
        super(u);
    }
}
