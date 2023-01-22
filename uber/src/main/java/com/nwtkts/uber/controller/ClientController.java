package com.nwtkts.uber.controller;

import com.nwtkts.uber.dto.*;
import com.nwtkts.uber.exception.BadRequestException;
import com.nwtkts.uber.exception.NotFoundException;
import com.nwtkts.uber.model.Client;
import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.User;
import com.nwtkts.uber.service.ClientService;
import com.nwtkts.uber.service.RideService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@RestController
@PreAuthorize("hasRole('ROLE_CLIENT')")
@RequestMapping(value = "/client", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private RideService rideService;


    @PostMapping("/finishSignUp")
    public ResponseEntity<Boolean> addAdditionalInfo(@RequestBody AdditionalRegInfoDTO clientInfo, Principal user) {
        Client client = clientService.findSummaryByEmail(user.getName());

        if (client == null) throw new BadRequestException("Bad request");

        if (clientService.updateClientWithAdditionalInfo(client, clientInfo))
            return new ResponseEntity<>(true, HttpStatus.OK);
        else return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/userByEmail")
    public ResponseEntity<UserProfile> getUserProfileForRideByEmail(Principal loggedInUser, @RequestParam("email") String email) {
        Client client = this.clientService.findSummaryByEmail(email.toLowerCase().trim());
        if (client == null) throw new NotFoundException("Client with this email doesn't exist!");
        if (loggedInUser.getName().equals(client.getEmail())) throw new BadRequestException("Bad request");
        return new ResponseEntity<>(new UserProfile(client), HttpStatus.OK);
    }

    @PostMapping("/buyTokens")
    public ResponseEntity<?> buyTokens(Principal user, @RequestBody TokenPurchaseDTO tokenPurchaseDto) {
        Client client = clientService.findWithTransactionsByEmail(user.getName());

        if (client == null) throw new BadRequestException("Not allowed for this user");

        clientService.addTokens(client, tokenPurchaseDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(
            path = "/fullWalletInfo",
            produces = "application/json"
    )
    public ResponseEntity<ClientsWalletDTO>getWalletInfo(Principal user) {
        Client client = clientService.findWithTransactionsByEmail(user.getName());

        if (client == null) throw new BadRequestException("Not allowed for this user");

        ClientsWalletDTO clientsWallet = this.clientService.getWalletInfo(client);

        return new ResponseEntity<>(clientsWallet, HttpStatus.OK);
    }

    @GetMapping(
            path = "/mySplitFareReqs",
            produces = "application/json"
    )
    public ResponseEntity<List<ClientsSplitFareRideDTO>>getSplitFareReqs(Principal user) {
        Client client = clientService.findSummaryByEmail(user.getName());
        if (client == null) throw new BadRequestException("Not allowed for this user");

        List<ClientsSplitFareRideDTO> retList = new ArrayList<>();

        List<Ride> clientsSplitFareReqs = this.rideService.getSplitFareRequestsForClient(client);
        for (Ride ride: clientsSplitFareReqs) {
            retList.add(new ClientsSplitFareRideDTO(ride));
        }
        return new ResponseEntity<>(retList, HttpStatus.OK);
    }

    @PostMapping(path = "/acceptSplitFareReq")
    public ResponseEntity<?>acceptSplitFareReq(Principal user, @RequestBody Long rideId) {
        Client client = clientService.findSummaryByEmail(user.getName());
        if (client == null) throw new BadRequestException("Not allowed for this user");

        this.rideService.acceptSplitFareReq(client, rideId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
