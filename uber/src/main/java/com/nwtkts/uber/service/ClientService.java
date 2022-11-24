package com.nwtkts.uber.service;

import com.nwtkts.uber.dto.RegistrationRequest;
import com.nwtkts.uber.model.Client;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface ClientService {
    Client register(RegistrationRequest userRequest, String siteURL) throws MessagingException, UnsupportedEncodingException;

    void sendVerificationEmail(Client client, String siteURL) throws MessagingException, UnsupportedEncodingException;

    boolean verify(String verificationCode);
}
