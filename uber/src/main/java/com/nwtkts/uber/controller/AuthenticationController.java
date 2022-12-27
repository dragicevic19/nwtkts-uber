package com.nwtkts.uber.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.nwtkts.uber.dto.*;
import com.nwtkts.uber.model.*;
import com.nwtkts.uber.service.ClientService;
import com.nwtkts.uber.service.PasswordResetTokenService;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.Collections;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {
    private static final String CLIENT_ID = "580010731527-g2pjimi8f9u1q1apl9urmfsse1birc6m.apps.googleusercontent.com";
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private PasswordResetTokenService passwordResetTokenService;


    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(), authenticationRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal();
            String jwt = tokenUtils.generateToken(user);
            int expiresIn = tokenUtils.getExpiredIn();

            return ResponseEntity.ok(new UserTokenState(jwt, expiresIn, user.isFullRegDone()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (DisabledException e) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("oauth2/google/login")
    public ResponseEntity<UserTokenState> googleLogin(@RequestBody String idTokenString) throws GeneralSecurityException, IOException {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
//            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());

            return socialLogin(new SocialSignInRequest(
                    (String) payload.get("given_name"),
                    (String) payload.get("family_name"),
                    payload.getEmail(),
                    (String) payload.get("picture")
            ));
        } else {
            System.out.println("Invalid ID token.");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("oauth2/facebook/login")
    public ResponseEntity<UserTokenState> facebookLogin(@RequestBody SocialSignInRequest userRequest) {
        return socialLogin(userRequest);
    }

    private ResponseEntity<UserTokenState> socialLogin(SocialSignInRequest userRequest) {
        Client client = clientService.findSummaryByEmail(userRequest.getEmail());

        if (client == null) {
            if (userService.findByEmail(userRequest.getEmail()) != null) // user exists but it's not client
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            else
                client = this.clientService.socialRegistration(userRequest); // make new client without password
        } else {
            clientService.updateClientWithSocialInfo(client, userRequest);
        }

        String jwt = tokenUtils.generateToken(client);
        int expiresIn = tokenUtils.getExpiredIn();
        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn, client.isFullRegDone()));
    }


    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    // Endpoint za registraciju klijenta
    @PostMapping("/signup")
    public ResponseEntity<RegistrationResponse> clientRegistration(@RequestBody RegistrationRequest userRequest,
                                                                   UriComponentsBuilder ucBuilder, HttpServletRequest request) {
        if (userService.checkIfUserExists(userRequest))
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        try {
            Client client = this.clientService.register(userRequest, getSiteURL(request));
            if (client != null)
                return new ResponseEntity<>(new RegistrationResponse(client.getFirstName(), client.getLastName(),
                        client.getEmail()), HttpStatus.CREATED);
            else
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (MessagingException | UnsupportedEncodingException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<Boolean> verifyClient(@Param("code") String code, HttpServletResponse res) {
        try {
            if (clientService.verify(code)) {
                res.sendRedirect("http://localhost:4200/login");
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<Boolean> forgotPassword(@RequestBody String email) {
        try {
            User user = userService.findByEmail(email);
            if (user == null)
                return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);

            userService.resetPasswordRequest(user);
            return new ResponseEntity<>(true, HttpStatus.OK);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<Boolean> resetPassword(@RequestBody ResetPasswordDto payload) {

        User user = userService.findByEmail(payload.getEmail());
        if (user == null) return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);

        PasswordResetToken token = passwordResetTokenService.validatePasswordResetToken(payload);
        if (token == null) return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);

        if (!payload.getPassword().equals(payload.getRepPassword()))
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);

        userService.resetPassword(user, payload.getPassword());
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/whoami")
    public ResponseEntity<UserProfile> loggedInUser(Principal user) {
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        User loggedInUser = this.userService.findByEmail(user.getName());
        if (loggedInUser == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new UserProfile(loggedInUser), HttpStatus.OK);
    }
}