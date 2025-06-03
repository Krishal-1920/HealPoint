package com.example.HealPoint.repository;

import com.example.HealPoint.entity.Slots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SlotsRepository extends JpaRepository<Slots, String> {

    @Query(value = "SELECT s.* FROM slots s " +
            "JOIN users u ON s.provider_id = u.user_id " +
            "WHERE s.status = 'AVAILABLE' AND (" +
            "  :search IS NULL OR " +
            "  LOWER(u.first_name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "  LOWER(u.last_name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "  LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "  LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "  u.phone_number LIKE CONCAT('%', :search, '%')" +
            ")",
            nativeQuery = true)
    List<Slots> searchSlots(@Param("search") String search);

}
