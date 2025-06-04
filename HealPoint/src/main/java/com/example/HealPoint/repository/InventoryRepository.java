package com.example.HealPoint.repository;

import com.example.HealPoint.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, String> {

    @Query(value = "SELECT * FROM inventory i WHERE " +
            "LOWER(i.product_name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(i.item_category) LIKE LOWER(CONCAT('%', :search, '%'))",
            nativeQuery = true)
    List<Inventory> searchList(@Param("search") String search);


}
