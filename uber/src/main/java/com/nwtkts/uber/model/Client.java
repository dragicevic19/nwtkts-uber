package com.nwtkts.uber.model;

import com.nwtkts.uber.model.enums.AuthenticationProvider;
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

    @Enumerated(EnumType.STRING)
    @Column
    private AuthenticationProvider authProvider;

}
