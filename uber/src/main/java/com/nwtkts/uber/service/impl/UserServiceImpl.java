package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.dto.RegistrationRequest;
import com.nwtkts.uber.model.Address;
import com.nwtkts.uber.model.User;
import com.nwtkts.uber.repository.UserRepository;
import com.nwtkts.uber.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

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
        u.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        u.setFirstName(userRequest.getFirstName());
        u.setLastName(userRequest.getLastName());
        u.setPhoneNumber(userRequest.getPhoneNumber());
        u.setAddress(new Address(userRequest.getStreet(), userRequest.getCity(), userRequest.getCountry()));
        u.setBlocked(false);
        u.setLastPasswordResetDate(Timestamp.valueOf(LocalDateTime.now()));
        return u;
    }
}
