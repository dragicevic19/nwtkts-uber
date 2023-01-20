package com.nwtkts.uber.repository;

import com.nwtkts.uber.model.Client;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

//    @Query("SELECT c FROM Client c WHERE c.verificationCode = ?1")
    Optional<Client> findByVerificationCode(String code);

    @EntityGraph(attributePaths = {"favoriteRoutes"})
    Client findDetailedByEmail(String email);

    Client findSummaryByEmail(String email);
}
