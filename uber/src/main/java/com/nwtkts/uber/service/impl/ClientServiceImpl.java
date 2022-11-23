package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.dto.ClientRegistrationRequest;
import com.nwtkts.uber.model.Address;
import com.nwtkts.uber.model.Client;
import com.nwtkts.uber.model.Role;
import com.nwtkts.uber.repository.ClientRepository;
import com.nwtkts.uber.service.ClientService;
import com.nwtkts.uber.service.RoleService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RoleService roleService;
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Client register(ClientRegistrationRequest userRequest, String siteURL) throws MessagingException, UnsupportedEncodingException {
        Client c = new Client();
        c.setEmail(userRequest.getEmail());
        // treba voditi racuna da se koristi isi password encoder bean koji je postavljen u AUthenticationManager-u kako bi koristili isti algoritam
        c.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        c.setFirstName(userRequest.getFirstName());
        c.setLastName(userRequest.getLastName());
        c.setPhoneNumber(userRequest.getPhoneNumber());
        c.setAddress(new Address(userRequest.getStreet(), userRequest.getCity(), userRequest.getCountry()));
        c.setBlocked(false);
        c.setLastPasswordResetDate(Timestamp.valueOf(LocalDateTime.now()));
        c.setFavoriteRoutes(new ArrayList<>());
        List<Role> roles = roleService.findByName("ROLE_CLIENT");
        c.setRoles(roles);

        c.setEnabled(false);
        String randomCode = RandomString.make(64);
        c.setVerificationCode(randomCode);

        clientRepository.save(c);
        sendVerificationEmail(c, siteURL);
        return c;
    }

    @Override
    public void sendVerificationEmail(Client client, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String toAddress = client.getEmail();
        String fromAddress = "uberappnwt@gmail.com";
        String senderName = "Uber Team";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>" +
                "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" +
                "Thank you,<br>" +
                "Your Uber Team.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", client.getFirstName());
        String verifyURL = siteURL + "/auth/verify?code=" + client.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
        mailSender.send(message);
    }

    @Override
    public boolean verify(String verificationCode) {
        Client client = clientRepository.findByVerificationCode(verificationCode);

        if (client == null || client.isEnabled()){
            return false;
        } else {
            client.setVerificationCode(null);
            client.setEnabled(true);
            clientRepository.save(client);
            return true;
        }
    }
}
