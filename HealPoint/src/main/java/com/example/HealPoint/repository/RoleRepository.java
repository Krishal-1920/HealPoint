package com.example.HealPoint.repository;

import com.example.HealPoint.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    List<Role> findAllByRoleIdIn(List<String> roleIdFromModel);

}
