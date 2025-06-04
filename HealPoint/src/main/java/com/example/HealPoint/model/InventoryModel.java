package com.example.HealPoint.model;

import lombok.Data;

@Data
public class InventoryModel {

    private String productName;
    private String itemCategory;
    private double itemQuantity;
    private String itemDescription;
    private double itemPrice;
    private double itemUnits;
    private boolean isAvailable;

}
