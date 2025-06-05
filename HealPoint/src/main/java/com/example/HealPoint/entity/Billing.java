package com.example.HealPoint.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "billings")
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "billing_id", updatable = false, nullable = false)
    private String billingId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "billing_date")
    private LocalDate billingDate;

    @Column(name = "total_amount")
    private double totalAmount;

    @OneToMany(mappedBy = "billing", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillingItem> billingItems;

}
