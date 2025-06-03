package com.example.HealPoint.repository;

import com.example.HealPoint.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, String> {
}
