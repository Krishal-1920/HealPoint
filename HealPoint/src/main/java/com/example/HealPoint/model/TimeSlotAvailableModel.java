package com.example.HealPoint.model;

import com.example.HealPoint.enums.Status;
import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeSlotAvailableModel {

    private LocalTime startTime;
    private LocalTime endTime;
    private Status status;

}
