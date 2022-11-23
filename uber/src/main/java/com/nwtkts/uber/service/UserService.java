package com.nwtkts.uber.service;

import com.nwtkts.uber.model.User;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface UserService {
    User findById(Long userId) throws AccessDeniedException;

    List<User> findAll() throws AccessDeniedException;

    User findByEmail(String name);

}
