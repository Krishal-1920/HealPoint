package com.example.HealPoint.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderHistoryDateModel {

    private LocalDate billDate;
    private List<BillingItemsModel> billingItemsModel;
    private String totalBill;
}
