package com.nwtkts.uber.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ClientTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    private LocalDate date;
    private Double amount;
    private String status;

    public ClientTransaction(LocalDate date, Double amount, String status) {
        this.date = date;
        this.amount = amount;
        this.status = status;
    }
}