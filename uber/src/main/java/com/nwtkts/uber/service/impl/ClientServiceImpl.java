package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.dto.*;
import com.nwtkts.uber.exception.BadRequestException;
import com.nwtkts.uber.exception.NotFoundException;
import com.nwtkts.uber.model.*;
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
import java.time.LocalDate;
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
        c.setTokens(0.);
        c.setTransactions(new ArrayList<>());
        clientRepository.save(c);
        emailService.sendVerificationEmail(c, siteURL);
        return c;
    }

    @Override
    @Transactional
    public boolean verify(String verificationCode) {
        Client client = clientRepository.findByVerificationCode(verificationCode).orElseThrow(
                () -> new NotFoundException("User with that verification code doesn't exist!"));

        if (client.isEnabled()) {
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
        c.setTokens(0.);
        c.setTransactions(new ArrayList<>());
        return clientRepository.save(c);
    }

    @Override
    public Client findSummaryByEmail(String email) {
        return clientRepository.findSummaryByEmail(email);
    }

    @Override
    public Client findDetailedByEmail(String email) {
        return clientRepository.findWithFavRoutesByEmail(email);
    }

    @Override
    public Client findWithTransactionsByEmail(String email) {
        return clientRepository.findWithTransactionsByEmail(email);
    }

    @Override
    public void updateClientWithSocialInfo(Client client, SocialSignInRequest userRequest) {
        client.setEnabled(true);
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

    @Override
    @Transactional
    public boolean makePayment(Client client, double amount) {
        if (client.getTokens() < amount)
            throw new BadRequestException("You don't have enough tokens. Buy more!");
        client.setTokens(client.getTokens() - amount);
        client = this.clientRepository.save(client);

        client = this.clientRepository.findWithTransactionsByEmail(client.getEmail());
        client.getTransactions().add(new ClientTransaction(LocalDate.now(), 0 - amount, "RIDE_REQUEST"));
        this.clientRepository.save(client);
        return true;
    }

    @Override
    public boolean refundForCanceledRide(Client client, double amount) {
        client = this.clientRepository.save(client);
        client = this.clientRepository.findWithTransactionsByEmail(client.getEmail());
        client.setTokens(client.getTokens() + amount);
        client.getTransactions().add(new ClientTransaction(LocalDate.now(), amount, "REFUND"));
        this.clientRepository.save(client);
        return true;
    }

    @Override
    public void refundToClients(Ride ride) {
        for (ClientRide clientOnRide : ride.getClientsInfo()) {
            if (clientOnRide.isClientPaid()) {
                refundForCanceledRide(clientOnRide.getClient(), ride.getPrice() / ride.getClientsInfo().size());
            }
        }
    }

    @Override
    public void addTokens(Client client, TokenPurchaseDTO tokenPurchaseDto) {
        client.setTokens(client.getTokens() + tokenPurchaseDto.getAmount());
        client.getTransactions().add(new ClientTransaction(LocalDate.now(),
                Double.valueOf(tokenPurchaseDto.getAmount()), "PURCHASED"));
        this.clientRepository.save(client);
    }

    @Override
    public ClientsWalletDTO getWalletInfo(Client client) {
        ClientsWalletDTO clientsWalletDTO = new ClientsWalletDTO();
        clientsWalletDTO.setCurrentBalance(client.getTokens());
        clientsWalletDTO.setSpentThisMonth(calcHowMuchClientSpentThisMonth(client));
        clientsWalletDTO.setSpentThisYear(calcHowMuchClientSpentThisYear(client));
        clientsWalletDTO.setTransactionHistory(client.getTransactions());
        return clientsWalletDTO;
    }


    private Double calcHowMuchClientSpentThisYear(Client client) {
        double res = 0.;
        for (ClientTransaction transaction : client.getTransactions()) {
            if (!transaction.getStatus().equals("PURCHASED")) {
                if (transaction.getDate().plusYears(1).isAfter(LocalDate.now())) {
                    res += transaction.getAmount();
                }
            }
        }
        return 0 - res;
    }

    private Double calcHowMuchClientSpentThisMonth(Client client) {
        double res = 0.;
        for (ClientTransaction transaction : client.getTransactions()) {
            if (!transaction.getStatus().equals("PURCHASED")) {
                if (transaction.getDate().plusMonths(1).isAfter(LocalDate.now())) {
                    res += transaction.getAmount();
                }
            }
        }
        return 0 - res;
    }
}
