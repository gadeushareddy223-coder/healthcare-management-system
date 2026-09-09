package com.healthcare.prescription.controller;

import com.healthcare.appointment.Appointment;
import com.healthcare.appointment.repository.AppointmentRepository;
import com.healthcare.prescription.Prescription;
import com.healthcare.prescription.repository.PrescriptionRepository;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;

    public PrescriptionController(
            PrescriptionRepository prescriptionRepository,
            AppointmentRepository appointmentRepository) {

        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // GET ALL PRESCRIPTIONS
    @GetMapping
    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    // GET PRESCRIPTION BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Prescription> getPrescriptionById(
            @PathVariable Long id) {

        return prescriptionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE PRESCRIPTION
    @PostMapping
    public ResponseEntity<Prescription> createPrescription(
            @Valid @RequestBody Prescription prescription) {

        if (prescription.getAppointment() == null ||
                prescription.getAppointment().getId() == null) {

            return ResponseEntity.badRequest().build();
        }

        Appointment appointment = appointmentRepository
                .findById(prescription.getAppointment().getId())
                .orElse(null);

        if (appointment == null) {
            return ResponseEntity.notFound().build();
        }

        prescription.setAppointment(appointment);

        Prescription savedPrescription =
                prescriptionRepository.save(prescription);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedPrescription);
    }

    // UPDATE PRESCRIPTION
    @PutMapping("/{id}")
    public ResponseEntity<Prescription> updatePrescription(
            @PathVariable Long id,
            @Valid @RequestBody Prescription prescriptionDetails) {

        return prescriptionRepository.findById(id)
                .map(prescription -> {

                    if (prescriptionDetails.getAppointment() != null &&
                            prescriptionDetails.getAppointment().getId() != null) {

                        Appointment appointment = appointmentRepository
                                .findById(
                                        prescriptionDetails
                                                .getAppointment()
                                                .getId())
                                .orElse(null);

                        if (appointment == null) {
                            return null;
                        }

                        prescription.setAppointment(appointment);
                    }

                    prescription.setMedicineName(
                            prescriptionDetails.getMedicineName());

                    prescription.setDosage(
                            prescriptionDetails.getDosage());

                    prescription.setFrequency(
                            prescriptionDetails.getFrequency());

                    prescription.setDuration(
                            prescriptionDetails.getDuration());

                    prescription.setInstructions(
                            prescriptionDetails.getInstructions());

                    Prescription updatedPrescription =
                            prescriptionRepository.save(prescription);

                    return ResponseEntity.ok(updatedPrescription);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE PRESCRIPTION
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(
            @PathVariable Long id) {

        if (!prescriptionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        prescriptionRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}