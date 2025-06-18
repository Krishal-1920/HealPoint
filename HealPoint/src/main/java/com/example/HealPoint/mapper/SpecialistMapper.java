package com.example.HealPoint.mapper;

import com.example.HealPoint.entity.Specialist;
import com.example.HealPoint.model.SpecialistModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SpecialistMapper {


    SpecialistMapper INSTANCE = Mappers.getMapper(SpecialistMapper.class);

    Specialist specialistModelToSpecialist(SpecialistModel specialistModel);

    SpecialistModel specialistToSpecialistModel(Specialist specialist);

}
