package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.model.User;
import com.nwtkts.uber.repository.UserRepository;
import com.nwtkts.uber.service.AdminService;
import com.nwtkts.uber.service.ClientService;
import com.nwtkts.uber.service.DriverService;
import com.nwtkts.uber.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private DriverService driverService;
    @Autowired
    private AdminService adminService;


    @Override
    public User findById(Long userId) throws AccessDeniedException {
        return userRepository.findById(userId).orElseGet(null);
    }

    @Override
    public List<User> findAll() throws AccessDeniedException {
        return userRepository.findAll();
    }

    @Override
    public User findByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }
}
