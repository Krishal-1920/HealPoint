package com.example.HealPoint.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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

    //Mapping
    @OneToMany(mappedBy = "specialist", cascade = CascadeType.ALL)
    private List<UserSpecialist> userSpecialist;


}
