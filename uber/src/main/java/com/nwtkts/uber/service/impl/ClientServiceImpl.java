package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.dto.AdditionalRegInfoDTO;
import com.nwtkts.uber.dto.RegistrationRequest;
import com.nwtkts.uber.dto.SocialSignInRequest;
import com.nwtkts.uber.model.Address;
import com.nwtkts.uber.model.Client;
import com.nwtkts.uber.model.Role;
import com.nwtkts.uber.model.enums.AuthenticationProvider;
import com.nwtkts.uber.repository.ClientRepository;
import com.nwtkts.uber.service.ClientService;
import com.nwtkts.uber.service.EmailService;
import com.nwtkts.uber.service.RoleService;
import com.nwtkts.uber.service.UserService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Client register(RegistrationRequest userRequest, String siteURL) throws MessagingException, UnsupportedEncodingException {
        Client c = new Client();
        c = (Client) userService.register(c, userRequest);
        c.setFavoriteRoutes(new ArrayList<>());
        List<Role> roles = roleService.findByName("ROLE_CLIENT");
        c.setRoles(roles);

        c.setAuthProvider(AuthenticationProvider.LOCAL);
        c.setEnabled(false);
        String randomCode = RandomString.make(64);
        c.setVerificationCode(randomCode);

        clientRepository.save(c);
        emailService.sendVerificationEmail(c, siteURL);
        return c;
    }

    @Override
    @Transactional
    public boolean verify(String verificationCode) {
        Client client = clientRepository.findByVerificationCode(verificationCode);

        if (client == null || client.isEnabled()) {
            return false;
        } else {
            client.setVerificationCode(null);
            client.setEnabled(true);
            clientRepository.save(client);
            return true;
        }
    }

    @Override
    public Client socialRegistration(SocialSignInRequest userRequest) {
        Client c = new Client();
        c = (Client) userService.register(c, userRequest);
        c.setPhoto(userRequest.getPicture());
        c.setAuthProvider(AuthenticationProvider.SOCIAL);
        c.setFavoriteRoutes(new ArrayList<>());
        List<Role> roles = roleService.findByName("ROLE_CLIENT");
        c.setRoles(roles);
        c.setEnabled(true);
        return clientRepository.save(c);
    }

    @Override
    public Client findSummaryByEmail(String email) {
        return clientRepository.findSummaryByEmail(email);
    }

    @Override
    public Client findDetailedByEmail(String email) {
        return clientRepository.findDetailedByEmail(email);
    }

    @Override
    public void updateClientWithSocialInfo(Client client, SocialSignInRequest userRequest) {
        client.setEnabled(true);
        client.setPhoto(userRequest.getPicture());
        client.setFirstName(userRequest.getFirstName());
        client.setLastName(userRequest.getLastName());
        clientRepository.save(client);
    }

    @Override
    public boolean updateClientWithAdditionalInfo(Client client, AdditionalRegInfoDTO clientInfo) {
        try {
            client.setAddress(new Address(clientInfo.getStreet(), clientInfo.getCity(), clientInfo.getCountry()));
            client.setPhoneNumber(clientInfo.getPhoneNumber());
            client.setFullRegDone(true);
            clientRepository.save(client);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}