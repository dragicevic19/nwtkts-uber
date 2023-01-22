package com.nwtkts.uber.dto;

import com.nwtkts.uber.model.ClientTransaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientsWalletDTO {

    private Double currentBalance;
    private Double spentThisMonth;
    private Double spentThisYear;
    private List<ClientTransaction> transactionHistory = new ArrayList<>();
}