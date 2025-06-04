package com.example.HealPoint.model;

import lombok.Data;

import java.util.List;

@Data
public class SlotBookingModel {

    private String providerId;
    private String providerName;
    private List<DateAvailableModel> dates;

}
