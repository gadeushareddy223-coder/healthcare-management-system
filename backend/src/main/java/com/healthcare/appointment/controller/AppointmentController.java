package com.healthcare.appointment.controller;

import com.healthcare.appointment.Appointment;
import com.healthcare.appointment.repository.AppointmentRepository;
import com.healthcare.doctor.Doctor;
import com.healthcare.doctor.repository.DoctorRepository;
import com.healthcare.patient.Patient;
import com.healthcare.patient.repository.PatientRepository;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public AppointmentController(
            AppointmentRepository appointmentRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository) {

        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    // GET ALL APPOINTMENTS
    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // GET APPOINTMENT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(
            @PathVariable Long id) {

        return appointmentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE APPOINTMENT
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(
            @Valid @RequestBody Appointment appointment) {

        if (appointment.getDoctor() == null ||
                appointment.getDoctor().getId() == null) {

            return ResponseEntity.badRequest().build();
        }

        if (appointment.getPatient() == null ||
                appointment.getPatient().getId() == null) {

            return ResponseEntity.badRequest().build();
        }

        Doctor doctor = doctorRepository
                .findById(appointment.getDoctor().getId())
                .orElse(null);

        if (doctor == null) {
            System.out.println(
                    "DEBUG: Doctor NOT FOUND during CREATE: "
                            + appointment.getDoctor().getId());

            return ResponseEntity.notFound().build();
        }

        Patient patient = patientRepository
                .findById(appointment.getPatient().getId())
                .orElse(null);

        if (patient == null) {
            System.out.println(
                    "DEBUG: Patient NOT FOUND during CREATE: "
                            + appointment.getPatient().getId());

            return ResponseEntity.notFound().build();
        }

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedAppointment);
    }

    // UPDATE APPOINTMENT
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody Appointment appointmentDetails) {

        System.out.println(
                "DEBUG: PUT /api/appointments/" + id);

        Appointment appointment = appointmentRepository
                .findById(id)
                .orElse(null);

        if (appointment == null) {
            System.out.println(
                    "DEBUG: Appointment NOT FOUND: " + id);

            return ResponseEntity.notFound().build();
        }

        System.out.println(
                "DEBUG: Appointment FOUND: " + id);

        if (appointmentDetails.getDoctor() == null ||
                appointmentDetails.getDoctor().getId() == null) {

            System.out.println(
                    "DEBUG: Doctor information is missing");

            return ResponseEntity.badRequest().build();
        }

        if (appointmentDetails.getPatient() == null ||
                appointmentDetails.getPatient().getId() == null) {

            System.out.println(
                    "DEBUG: Patient information is missing");

            return ResponseEntity.badRequest().build();
        }

        System.out.println(
                "DEBUG: Doctor ID received: "
                        + appointmentDetails.getDoctor().getId());

        Doctor doctor = doctorRepository
                .findById(appointmentDetails.getDoctor().getId())
                .orElse(null);

        if (doctor == null) {
            System.out.println(
                    "DEBUG: Doctor NOT FOUND: "
                            + appointmentDetails.getDoctor().getId());

            return ResponseEntity.notFound().build();
        }

        System.out.println(
                "DEBUG: Doctor FOUND: "
                        + doctor.getId());

        System.out.println(
                "DEBUG: Patient ID received: "
                        + appointmentDetails.getPatient().getId());

        Patient patient = patientRepository
                .findById(appointmentDetails.getPatient().getId())
                .orElse(null);

        if (patient == null) {
            System.out.println(
                    "DEBUG: Patient NOT FOUND: "
                            + appointmentDetails.getPatient().getId());

            return ResponseEntity.notFound().build();
        }

        System.out.println(
                "DEBUG: Patient FOUND: "
                        + patient.getId());

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        appointment.setAppointmentDate(
                appointmentDetails.getAppointmentDate());

        appointment.setStatus(
                appointmentDetails.getStatus());

        appointment.setReason(
                appointmentDetails.getReason());

        appointment.setNotes(
                appointmentDetails.getNotes());

        System.out.println(
                "DEBUG: Saving updated appointment");

        Appointment updatedAppointment =
                appointmentRepository.save(appointment);

        System.out.println(
                "DEBUG: Appointment UPDATED successfully");

        return ResponseEntity.ok(updatedAppointment);
    }

    // DELETE APPOINTMENT
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(
            @PathVariable Long id) {

        if (!appointmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        appointmentRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}