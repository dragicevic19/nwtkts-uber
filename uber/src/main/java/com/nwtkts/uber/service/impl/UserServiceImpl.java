package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.dto.RegistrationRequest;
import com.nwtkts.uber.model.User;
import com.nwtkts.uber.repository.UserRepository;
import com.nwtkts.uber.service.EmailService;
import com.nwtkts.uber.service.PasswordResetTokenService;
import com.nwtkts.uber.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordResetTokenService passwordResetTokenService;
    @Autowired
    private EmailService emailService;


    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseGet(null);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }

    @Override
    public User register(User u, RegistrationRequest userRequest) {
        u.setEmail(userRequest.getEmail());
        if (userRequest.getPassword() != null)
            u.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        u.setFirstName(userRequest.getFirstName());
        u.setLastName(userRequest.getLastName());
        u.setBlocked(false);
        u.setFullRegDone(false);
        u.setLastPasswordResetDate(Timestamp.valueOf(LocalDateTime.now().minusSeconds(1)));
        u.setPhoto("https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava3.webp");
        return u;
    }

    @Override
    public boolean checkIfUserExists(RegistrationRequest userRequest) {
        userRequest.setEmail(userRequest.getEmail().toLowerCase(Locale.ROOT));
        User existUser = this.userRepository.findByEmail(userRequest.getEmail());
        return existUser != null;
    }

    @Override
    public void resetPasswordRequest(User user) throws MessagingException, UnsupportedEncodingException {
        String token = passwordResetTokenService.generateToken(user);
        emailService.sendForgotPasswordMail(user, token);
    }

    @Override
    public void changePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        user.setLastPasswordResetDate(Timestamp.valueOf(LocalDateTime.now().minusSeconds(1)));
        userRepository.save(user);
    }
}
