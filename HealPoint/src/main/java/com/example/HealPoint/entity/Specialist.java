package com.example.HealPoint.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "specialist")
public class Specialist {

    @Id
    @Column(name = "specialist_id", nullable = false, updatable = false)
    private String specialistId;

    @Column(name = "Speciality")
    private String Specialty;

    @Column(name = "description")
    private String description;

}
