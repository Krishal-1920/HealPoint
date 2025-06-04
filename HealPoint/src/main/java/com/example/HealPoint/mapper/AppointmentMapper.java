package com.example.HealPoint.mapper;

import com.example.HealPoint.entity.Appointment;
import com.example.HealPoint.model.AppointmentModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    @Mapping(target = "providerName", source = "slot.user.username")
    AppointmentModel appointmentToAppointmentModel(Appointment appointment);

    Appointment appointmentModelToAppointment(AppointmentModel appointmentModel);

}
