package com.nwtkts.uber.service;

import com.nwtkts.uber.model.Client;
import com.nwtkts.uber.model.User;
import org.springframework.scheduling.annotation.Async;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailService {
    @Async
    void sendVerificationEmail(Client client, String siteURL) throws MessagingException, UnsupportedEncodingException;
    @Async
    void sendForgotPasswordMail(User user, String token) throws MessagingException, UnsupportedEncodingException;

}
