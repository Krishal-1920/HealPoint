package com.example.HealPoint.controller;

import com.example.HealPoint.model.AppointmentModel;
import com.example.HealPoint.model.SlotBookingModel;
import com.example.HealPoint.service.AppointmentService;
import com.example.HealPoint.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    private final JwtUtil jwtUtil;

    @PostMapping("/bookAnAppointment")
    public ResponseEntity<AppointmentModel> bookAnAppointment(@RequestHeader("Authorization") String email,
                                                              @RequestParam String slotId) {
        String authenticatedEmail = jwtUtil.extractUsername(email);
        return ResponseEntity.ok(appointmentService.bookAnAppointment(authenticatedEmail, slotId));
    }

    @PutMapping("/updateSlot")
    public ResponseEntity<AppointmentModel> updateSlot(@RequestHeader("Authorization") String tokenHeader,
                                                      @RequestParam String slotId) {
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(appointmentService.updateSlot(authenticatedEmail, slotId));
    }

    @GetMapping("/getAllBookings")
    public ResponseEntity<List<SlotBookingModel>> getAllBookings(@RequestHeader("Authorization") String tokenHeader,
                                                                 @RequestParam(required = false) String providerId) {
        String authenticatedEmail = jwtUtil.extractUsername(tokenHeader);
        return ResponseEntity.ok(appointmentService.getAllBookings(providerId));
    }

}
