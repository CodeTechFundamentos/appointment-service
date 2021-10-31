package com.nutrix.command.application.services;

import com.nutrix.command.domain.Appointment;
import com.nutrix.command.infra.IAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AppointmentCommandService extends Appointment {

    @Autowired
    private IAppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public Appointment save(Appointment appointment) throws Exception {
        return appointmentRepository.save(appointment);
    }

    @Override
    @Transactional
    public void delete(Integer id) throws Exception {
        appointmentRepository.deleteById(id);
    }
}
