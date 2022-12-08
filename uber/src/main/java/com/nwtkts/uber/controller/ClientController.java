package com.nwtkts.uber.controller;

import com.nwtkts.uber.dto.AdditionalRegInfoDTO;
import com.nwtkts.uber.dto.DriverRegistrationDTO;
import com.nwtkts.uber.model.Client;
import com.nwtkts.uber.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
@PreAuthorize("hasRole('ROLE_CLIENT')")
@RequestMapping(value = "/client", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/finishSignUp")
    public ResponseEntity<Boolean> addAdditionalInfo(@RequestBody AdditionalRegInfoDTO clientInfo, Principal user) {
        Client client = clientService.findByEmail(user.getName());

        if (client == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // ne moze se nikad desiti?
        }

        if (clientService.updateClientWithAdditionalInfo(client, clientInfo))
            return new ResponseEntity<>(true, HttpStatus.OK);
        else return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
