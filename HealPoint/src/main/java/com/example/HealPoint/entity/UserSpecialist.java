package com.example.HealPoint.entity;

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

    // Mapping
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "specialist_id", nullable = false)
    private Specialist specialist;

}
