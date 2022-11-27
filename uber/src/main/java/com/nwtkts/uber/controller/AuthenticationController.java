package com.nwtkts.uber.controller;

import com.nwtkts.uber.dto.RegistrationRequest;
import com.nwtkts.uber.model.*;
import com.nwtkts.uber.dto.JwtAuthenticationRequest;
import com.nwtkts.uber.dto.UserTokenState;
import com.nwtkts.uber.service.ClientService;
import com.nwtkts.uber.service.UserService;
import com.nwtkts.uber.util.TokenUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private ClientService clientService;


    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(), authenticationRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal();
            String jwt = tokenUtils.generateToken(user.getUsername());
            int expiresIn = tokenUtils.getExpiredIn();

            return ResponseEntity.ok(new UserTokenState(jwt, expiresIn, user));
        } catch (BadCredentialsException | DisabledException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint za registraciju klijenta
    @PostMapping("/signup")
    public ResponseEntity<Boolean> clientRegistration(@RequestBody RegistrationRequest userRequest,
                                                      UriComponentsBuilder ucBuilder, HttpServletRequest request) {
        userRequest.setEmail(userRequest.getEmail().toLowerCase(Locale.ROOT));
        User existUser = this.userService.findByEmail(userRequest.getEmail());

        if (existUser != null)
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        try {
            Client client = this.clientService.register(userRequest, getSiteURL(request));
            if (client != null)
                return new ResponseEntity<>(true, HttpStatus.CREATED);
            else
                return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (MessagingException | UnsupportedEncodingException e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping("/verify")
    public ResponseEntity<Boolean> verifyClient(@Param("code") String code) {
        if (clientService.verify(code)) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.CONFLICT);
        }
    }
}