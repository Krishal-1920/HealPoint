package com.example.HealPoint.repository;

import com.example.HealPoint.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

    Cart findByUserUserIdAndInventoryItemId(String userId, String itemId);

    List<Cart> findByUserUserId(String userId);
}
