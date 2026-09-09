package com.healthcare.medicalhistory;

import com.healthcare.medicalhistory.repository.MedicalHistoryRepository;
import com.healthcare.patient.Patient;
import com.healthcare.patient.repository.PatientRepository;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-history")
public class MedicalHistoryController {

    private final MedicalHistoryRepository medicalHistoryRepository;
    private final PatientRepository patientRepository;

    public MedicalHistoryController(
            MedicalHistoryRepository medicalHistoryRepository,
            PatientRepository patientRepository) {

        this.medicalHistoryRepository = medicalHistoryRepository;
        this.patientRepository = patientRepository;
    }

    // GET ALL MEDICAL HISTORY
    @GetMapping
    public List<MedicalHistory> getAllMedicalHistory() {
        return medicalHistoryRepository.findAll();
    }

    // GET MEDICAL HISTORY BY ID
    @GetMapping("/{id}")
    public ResponseEntity<MedicalHistory> getMedicalHistoryById(
            @PathVariable Long id) {

        return medicalHistoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE MEDICAL HISTORY
    @PostMapping
    public ResponseEntity<MedicalHistory> createMedicalHistory(
            @Valid @RequestBody MedicalHistory medicalHistory) {

        if (medicalHistory.getPatient() == null ||
                medicalHistory.getPatient().getId() == null) {

            return ResponseEntity.badRequest().build();
        }

        Patient patient = patientRepository
                .findById(medicalHistory.getPatient().getId())
                .orElse(null);

        if (patient == null) {
            return ResponseEntity.notFound().build();
        }

        medicalHistory.setPatient(patient);

        MedicalHistory savedMedicalHistory =
                medicalHistoryRepository.save(medicalHistory);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedMedicalHistory);
    }

    // UPDATE MEDICAL HISTORY
    @PutMapping("/{id}")
    public ResponseEntity<MedicalHistory> updateMedicalHistory(
            @PathVariable Long id,
            @Valid @RequestBody MedicalHistory medicalHistoryDetails) {

        return medicalHistoryRepository.findById(id)
                .map(medicalHistory -> {

                    if (medicalHistoryDetails.getPatient() != null &&
                            medicalHistoryDetails.getPatient().getId() != null) {

                        Patient patient = patientRepository
                                .findById(
                                        medicalHistoryDetails
                                                .getPatient()
                                                .getId())
                                .orElse(null);

                        if (patient == null) {
                            return null;
                        }

                        medicalHistory.setPatient(patient);
                    }

                    medicalHistory.setDiagnosis(
                            medicalHistoryDetails.getDiagnosis());

                    medicalHistory.setTreatment(
                            medicalHistoryDetails.getTreatment());

                    medicalHistory.setNotes(
                            medicalHistoryDetails.getNotes());

                    MedicalHistory updatedMedicalHistory =
                            medicalHistoryRepository.save(medicalHistory);

                    return ResponseEntity.ok(updatedMedicalHistory);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE MEDICAL HISTORY
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalHistory(
            @PathVariable Long id) {

        if (!medicalHistoryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        medicalHistoryRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}