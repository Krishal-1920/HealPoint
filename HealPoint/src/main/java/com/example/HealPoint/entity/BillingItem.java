package com.example.HealPoint.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "billing_items")
public class BillingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "billing_item_id", updatable = false, nullable = false)
    private String billingItemId;

    @ManyToOne
    @JoinColumn(name = "billing_id", nullable = false)
    private Billing billing;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Inventory inventory;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "price_at_purchase")
    private double priceAtPurchase;

    @Column(name = "quantity")
    private double quantity;
}
