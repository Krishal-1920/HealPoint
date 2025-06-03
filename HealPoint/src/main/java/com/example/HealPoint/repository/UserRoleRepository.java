package com.example.HealPoint.repository;

import com.example.HealPoint.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, String> {

    List<UserRole> findByUserUserId(String userId);

    void deleteByRoleRoleIdInAndUserUserId(List<String> removeRoleIds, String userId);
}
