package com.nwtkts.uber.repository;

import com.nwtkts.uber.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClientRepository extends JpaRepository<Client, Long> {

//    @Query("SELECT c FROM Client c WHERE c.verificationCode = ?1")
    public Client findByVerificationCode(String code);

    Client findByEmail(String email);
}
