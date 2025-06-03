package com.example.HealPoint.service;

import com.example.HealPoint.entity.Role;
import com.example.HealPoint.mapper.RoleMapper;
import com.example.HealPoint.model.RoleModel;
import com.example.HealPoint.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    public List<RoleModel> getRoles() {

        List<Role> allRoles = roleRepository.findAll();

        return allRoles.stream().map(role -> roleMapper.roleToRoleModel(role)).toList();
    }

}
