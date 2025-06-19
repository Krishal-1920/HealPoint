package com.example.HealPoint.entity;

import com.example.HealPoint.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user_specialist")
public class UserSpecialist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_specialist_id", updatable = false, nullable = false)
    private String userSpecialistId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.ACTIVE;

    // Mapping
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "specialist_id", nullable = false)
    private Specialist specialist;

}
