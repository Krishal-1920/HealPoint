package com.example.HealPoint.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DateAvailableModel {

    private LocalDate date;
    private List<TimeSlotAvailableModel> times;

}
