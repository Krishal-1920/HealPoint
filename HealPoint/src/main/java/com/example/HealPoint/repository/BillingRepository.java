package com.example.HealPoint.repository;

import com.example.HealPoint.entity.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingRepository extends JpaRepository<Billing, String> {

    List<Billing> findByUserUserId(String userId);

}
