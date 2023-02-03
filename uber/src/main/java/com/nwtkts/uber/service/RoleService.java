package com.nwtkts.uber.service;

import com.nwtkts.uber.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> findByName(String name);
}
