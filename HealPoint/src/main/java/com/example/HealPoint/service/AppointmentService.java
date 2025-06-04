package com.example.HealPoint.service;

import com.example.HealPoint.entity.Appointment;
import com.example.HealPoint.entity.Slots;
import com.example.HealPoint.entity.User;
import com.example.HealPoint.enums.BookingStatus;
import com.example.HealPoint.enums.Status;
import com.example.HealPoint.mapper.AppointmentMapper;
import com.example.HealPoint.mapper.SlotsMapper;
import com.example.HealPoint.model.AppointmentModel;
import com.example.HealPoint.model.DateAvailableModel;
import com.example.HealPoint.model.SlotBookingModel;
import com.example.HealPoint.model.TimeSlotAvailableModel;
import com.example.HealPoint.repository.AppointmentRepository;
import com.example.HealPoint.repository.SlotsRepository;
import com.example.HealPoint.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        Slots slots = slotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        if (slots.getStatus() == Status.BOOKED) {
            throw new RuntimeException("Slot is already booked");
        }

        slots.setStatus(Status.BOOKED);
        slotRepository.save(slots);

        Appointment appointment = new Appointment();
        appointment.setProviderUsername(slots.getProviderUsername());
        appointment.setUser(user);
        appointment.setSlot(slots);
        appointment.setStatus(BookingStatus.BOOKED);

        appointmentRepository.save(appointment);

        return appointmentMapper.appointmentToAppointmentModel(appointment);
    }


    public AppointmentModel updateSlot(String userId, String slotId) {
        User user = userRepository.findById(userId)
               .orElseThrow(() -> new RuntimeException("User not found"));

        Slots slots = slotRepository.findById(slotId)
              .orElseThrow(() -> new RuntimeException("Slot not found"));

        if(slots.getStatus() == Status.AVAILABLE){
            throw new RuntimeException("Slot is already available");
        }

        slots.setStatus(Status.AVAILABLE);
        slotRepository.save(slots);

        Appointment appointment = appointmentRepository.findByUserUserIdAndSlotSlotId(userId, slotId);

        appointment.setStatus(BookingStatus.CANCELLED_BOOKING);

        appointmentRepository.save(appointment);
        return appointmentMapper.appointmentToAppointmentModel(appointment);

    }


    public List<SlotBookingModel> getAllBookings(String userId) {
        List<Slots> slots;

        if (userId != null && !userId.isEmpty()) {
            slots = slotRepository.findByUserUserId(userId);
        } else {
            slots = slotRepository.findAll();
        }
        if (slots.isEmpty()) {
            return new ArrayList<>();
        }

        Map<User, List<Slots>> slotsByProvider = slots.stream()
                .collect(Collectors.groupingBy(Slots -> Slots.getUser()));

        List<SlotBookingModel> result = new ArrayList<>();

        for (Map.Entry<User, List<Slots>> entry : slotsByProvider.entrySet()) {
            User provider = entry.getKey();
            List<Slots> providerSlots = entry.getValue();

            // Group slots by date
            Map<LocalDate, List<Slots>> slotsByDate = providerSlots.stream()
                    .collect(Collectors.groupingBy(Slots-> Slots.getDate()));

            List<DateAvailableModel> dateAvailableModels = slotsByDate.entrySet().stream()
                    .map(entryDate -> {
                        LocalDate date = entryDate.getKey();
                        List<Slots> slotsForDate = entryDate.getValue();

                        List<TimeSlotAvailableModel> timeSlotAvailableModels = slotsForDate.stream()
                                .map(slot -> {
                                    TimeSlotAvailableModel timeModel = new TimeSlotAvailableModel();
                                    timeModel.setStartTime(slot.getStartTime());
                                    timeModel.setEndTime(slot.getEndTime());
                                    timeModel.setStatus(slot.getStatus());
                                    return timeModel;
                                })
                                .collect(Collectors.toList());

                        DateAvailableModel dateModel = new DateAvailableModel();
                        dateModel.setDate(date);
                        dateModel.setTimes(timeSlotAvailableModels);
                        return dateModel;
                    })
                    .collect(Collectors.toList());

            SlotBookingModel model = new SlotBookingModel();
            model.setProviderId(provider.getUserId());
            model.setProviderName(provider.getUsername());
            model.setDates(dateAvailableModels);
            result.add(model);
        }
        return result;
    }


}