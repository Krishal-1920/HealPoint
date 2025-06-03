package com.example.HealPoint.model;

import com.example.HealPoint.enums.Status;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SlotsModel {


    private String providerId;
    private String providerUsername;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Status status;

}
