package com.nwtkts.uber.repository;

import com.nwtkts.uber.model.Client;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

//    @Query("SELECT c FROM Client c WHERE c.verificationCode = ?1")
    Optional<Client> findByVerificationCode(String code);

    Client findSummaryByEmail(String email);

    @EntityGraph(attributePaths = {"favoriteRoutes"})
    Client findWithFavRoutesByEmail(String email);

    @EntityGraph(attributePaths = {"transactions"})
    Client findWithTransactionsByEmail(String email);
}
