package com.example.HealPoint.service;

import com.example.HealPoint.entity.Specialist;
import com.example.HealPoint.mapper.SpecialistMapper;
import com.example.HealPoint.model.SpecialistModel;
import com.example.HealPoint.repository.SpecialistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialistService {

    private final SpecialistRepository specialistRepository;

    private final SpecialistMapper specialistMapper;

    public List<SpecialistModel> getSpeciality(String search) {
        List<Specialist> all = specialistRepository.searchAll(search);
        return all.stream().map(speciality -> specialistMapper.specialistToSpecialistModel(speciality)).toList();
    }
}
