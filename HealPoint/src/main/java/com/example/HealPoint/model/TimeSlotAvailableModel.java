package com.example.HealPoint.model;

import com.example.HealPoint.enums.BookingStatus;
import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeSlotAvailableModel {

    private LocalTime startTime;
    private LocalTime endTime;
    private BookingStatus status;

}
