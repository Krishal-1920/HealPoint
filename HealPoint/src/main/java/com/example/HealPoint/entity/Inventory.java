package com.example.HealPoint.entity;

import com.example.HealPoint.enums.ItemCategory;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "item_id", updatable = false, nullable = false)
    private String itemId;

    @Column(name = "product_name")
    private String productName;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_category")
    private ItemCategory itemCategory;

    @Column(name = "item_quantity")
    private double itemQuantity;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "item_units")
    private double itemUnits;

    @Column(name = "item_price")
    private double itemPrice;

    @Column(name = "is_available")
    private boolean isAvailable;

    @PrePersist
    @PreUpdate
    private void updateAvailability() {
        this.isAvailable = this.itemQuantity >= 1;
    }


}
