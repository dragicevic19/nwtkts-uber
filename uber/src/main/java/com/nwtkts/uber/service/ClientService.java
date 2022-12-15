package com.nwtkts.uber.service;

import com.nwtkts.uber.dto.AdditionalRegInfoDTO;
import com.nwtkts.uber.dto.RegistrationRequest;
import com.nwtkts.uber.dto.SocialSignInRequest;
import com.nwtkts.uber.model.Client;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface ClientService {
    Client register(RegistrationRequest userRequest, String siteURL) throws MessagingException, UnsupportedEncodingException;


    boolean verify(String verificationCode);

    Client socialRegistration(SocialSignInRequest userRequest);

    Client findByEmail(String email);

    void updateClientWithSocialInfo(Client client, SocialSignInRequest userRequest);

    boolean updateClientWithAdditionalInfo(Client client, AdditionalRegInfoDTO clientInfo);
}
