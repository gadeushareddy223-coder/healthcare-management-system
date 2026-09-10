package com.healthcare.appointment.repository;

import com.healthcare.appointment.Appointment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    long countByStatus(String status);

}