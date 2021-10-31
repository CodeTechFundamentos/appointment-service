package com.nutrix.query.application.services;

import com.nutrix.command.domain.Appointment;
import com.nutrix.command.infra.IAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AppointmentQueryService extends Appointment {

    @Autowired
    private IAppointmentRepository appointmentRepository;

    @Override
    public List<Appointment> getAll() throws Exception {
        return appointmentRepository.findAll();
    }

    @Override
    public Optional<Appointment> getById(Integer id) throws Exception {
        return appointmentRepository.findById(id);
    }

    @Override
    public List<Appointment> findBetweenDates(Date date1, Date date2) throws Exception {
        return appointmentRepository.findBetweenDates(date1, date2);
    }

    @Override
    public List<Appointment> findByPatient(Integer patient_id) throws Exception {
        return appointmentRepository.findByPatient(patient_id);
    }

    @Override
    public List<Appointment> findByNutritionist(Integer nutritionist_id) throws Exception {
        return appointmentRepository.findByNutritionist(nutritionist_id);
    }
}
