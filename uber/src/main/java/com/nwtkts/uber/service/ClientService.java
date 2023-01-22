package com.nwtkts.uber.service;

import com.nwtkts.uber.dto.*;
import com.nwtkts.uber.model.Client;
import com.nwtkts.uber.model.Ride;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface ClientService {
    Client register(RegistrationRequest userRequest, String siteURL) throws MessagingException, UnsupportedEncodingException;
    boolean verify(String verificationCode);
    Client socialRegistration(SocialSignInRequest userRequest);
    Client findSummaryByEmail(String email);
    Client findDetailedByEmail(String email);
    void updateClientWithSocialInfo(Client client, SocialSignInRequest userRequest);
    boolean updateClientWithAdditionalInfo(Client client, AdditionalRegInfoDTO clientInfo);
    boolean makePayment(Client client, double amount);

    boolean refundForCanceledRide(Client client, double pricePerPerson);

    Client findWithTransactionsByEmail(String name);

    void addTokens(Client client, TokenPurchaseDTO tokenPurchaseDto);

    ClientsWalletDTO getWalletInfo(Client client);

    void refundToClients(Ride ride);
}
