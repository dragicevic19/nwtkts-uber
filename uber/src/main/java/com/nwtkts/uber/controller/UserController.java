package com.nwtkts.uber.controller;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

import com.nwtkts.uber.dto.UserProfile;
import com.nwtkts.uber.model.User;
import com.nwtkts.uber.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public User loadById(@PathVariable Long userId) {
        try {
            return this.userService.findById(userId);
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> loadAll() {
        try {
            return this.userService.findAll();
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/whoami")
    public ResponseEntity<UserProfile> loggedInUser(Principal user){
        User loggedInUser = this.userService.findByEmail(user.getName());
        if (loggedInUser == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new UserProfile(loggedInUser), HttpStatus.OK);
    }
}