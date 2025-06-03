package com.example.HealPoint.mapper;

import com.example.HealPoint.entity.Role;
import com.example.HealPoint.model.RoleModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleModel roleToRoleModel(Role role);

}
