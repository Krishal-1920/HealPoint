package com.example.HealPoint.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BillingModel {

    private String username;
    private LocalDate date;
    private String address;
    private String phoneNumber;
    private List<BillingItemsModel> billingItemsModels;
    private double totalPrice;

}
