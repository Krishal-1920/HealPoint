package com.example.HealPoint.controller;

import com.example.HealPoint.entity.Appointment;
import com.example.HealPoint.model.AppointmentModel;
import com.example.HealPoint.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/bookAnAppointment")
    public ResponseEntity<AppointmentModel> bookAnAppointment(@RequestParam String userId,
                                                              @RequestParam String slotId) {
        return ResponseEntity.ok(appointmentService.bookAnAppointment(userId, slotId));
    }

}
