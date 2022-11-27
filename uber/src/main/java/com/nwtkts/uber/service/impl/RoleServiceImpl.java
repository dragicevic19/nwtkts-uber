package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.model.Role;
import com.nwtkts.uber.repository.RoleRepository;
import com.nwtkts.uber.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
}
