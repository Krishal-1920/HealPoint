package com.example.HealPoint.model;

import lombok.Data;

import java.util.List;

@Data
public class CartModel {

    private String username;
    private List<ItemListModel> itemListModel;

}
