package com.example.HealPoint.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderHistoryModel {

    private String username;
    private List<OrderHistoryDateModel> orderHistoryDateModel;

}
