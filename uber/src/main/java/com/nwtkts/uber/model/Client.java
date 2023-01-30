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
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = true)
    protected List<Route> favoriteRoutes;

    @Enumerated(EnumType.STRING)
    @Column
    private AuthenticationProvider authProvider;

    @Column(name = "tokens")
    private Double tokens;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private List<ClientTransaction> transactions;

    public Client(User u, String verCode, List<Route> favRoute, AuthenticationProvider authProvider, Double tokens,
                  List<ClientTransaction> transactions) {
        super(u);
        this.verificationCode = verCode;
        this.favoriteRoutes = favRoute;
        this.authProvider = authProvider;
        this.tokens = tokens;
        this.transactions = transactions;
    }

}
