package com.example.HealPoint.repository;

import com.example.HealPoint.entity.UserSpecialist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSpecialistRepository extends JpaRepository<UserSpecialist, String> {
    List<UserSpecialist> findByUserUserId(String userId);
}
