package com.example.HealPoint.service;

import com.example.HealPoint.entity.Appointment;
import com.example.HealPoint.entity.Slots;
import com.example.HealPoint.entity.User;
import com.example.HealPoint.enums.Status;
import com.example.HealPoint.mapper.AppointmentMapper;
import com.example.HealPoint.mapper.SlotsMapper;
import com.example.HealPoint.model.AppointmentModel;
import com.example.HealPoint.repository.AppointmentRepository;
import com.example.HealPoint.repository.SlotsRepository;
import com.example.HealPoint.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final UserRepository userRepository;

    private final SlotsMapper slotMapper;

    private final SlotsRepository slotRepository;

    private final AppointmentRepository appointmentRepository;

    private final AppointmentMapper appointmentMapper;

    public AppointmentModel bookAnAppointment(String userId, String slotId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Slots slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        // Mark the slot as booked
        slot.setStatus(Status.BOOKED);
        slotRepository.save(slot);

        // Create and save the appointment
        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setSlot(slot);
        appointment.setBookingDate(LocalDate.now());

        appointmentRepository.save(appointment);

        // Build AppointmentModel manually or using mapper
        AppointmentModel model = new AppointmentModel();
        model.setBookingDate(appointment.getBookingDate());
        model.setProviderName(appointment.getSlot().getProviderUsername());


        return model;
    }


}