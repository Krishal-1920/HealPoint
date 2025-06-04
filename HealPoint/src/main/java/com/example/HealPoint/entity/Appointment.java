package com.example.HealPoint.entity;

import com.example.HealPoint.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "appointment_id", updatable = false, nullable = false)
    private String appointmentId;

    @Column(name = "booking_date")
    private LocalDate bookingDate;

    @Column(name = "provider_username")
    private String providerUsername;
    
    // Mapping
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slots slot;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @PrePersist
    protected void prePersist() {
        this.bookingDate = LocalDate.now();
    }

}
