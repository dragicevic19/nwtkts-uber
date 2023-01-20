package com.nwtkts.uber.controller;

import com.nwtkts.uber.dto.AdditionalRegInfoDTO;
import com.nwtkts.uber.dto.UserProfile;
import com.nwtkts.uber.exception.BadRequestException;
import com.nwtkts.uber.exception.NotFoundException;
import com.nwtkts.uber.model.Client;
import com.nwtkts.uber.model.User;
import com.nwtkts.uber.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@PreAuthorize("hasRole('ROLE_CLIENT')")
@RequestMapping(value = "/client", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/finishSignUp")
    public ResponseEntity<Boolean> addAdditionalInfo(@RequestBody AdditionalRegInfoDTO clientInfo, Principal user) {
        Client client = clientService.findSummaryByEmail(user.getName());

        if (client == null) {
            throw new BadRequestException("Bad request");
        }

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
}
