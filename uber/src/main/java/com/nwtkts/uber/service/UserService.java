package com.nwtkts.uber.service;

import com.nwtkts.uber.dto.RegistrationRequest;
import com.nwtkts.uber.model.User;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.nio.file.AccessDeniedException;
import java.util.List;

public interface UserService {
    User findById(Long userId) throws AccessDeniedException;
    List<User> findAll() throws AccessDeniedException;
    User findByEmail(String name);
    User register(User d, RegistrationRequest userRequest);
    boolean checkIfUserExists(RegistrationRequest userRequest);
    void resetPasswordRequest(User user) throws MessagingException, UnsupportedEncodingException;
    void changePassword(User user, String password);
}
