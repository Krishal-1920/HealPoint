package com.example.HealPoint.controller;

import com.example.HealPoint.entity.Appointment;
import com.example.HealPoint.model.AppointmentModel;
import com.example.HealPoint.model.SlotBookingModel;
import com.example.HealPoint.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/updateSlot")
    public ResponseEntity<AppointmentModel> updateSlot(@RequestParam String userId,
                                                      @RequestParam String slotId) {
        return ResponseEntity.ok(appointmentService.updateSlot(userId, slotId));
    }

    @GetMapping("/getAllBookings")
    public ResponseEntity<List<SlotBookingModel>> getAllBookings(@RequestParam(required = false) String providerId) {
        return ResponseEntity.ok(appointmentService.getAllBookings(providerId));
    }

}
