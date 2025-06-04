package com.example.HealPoint.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AppointmentModel {

    private String providerName;
    private LocalDate bookingDate;

}
