package com.healthcare.prescription.repository;

import com.healthcare.prescription.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository
        extends JpaRepository<Prescription, Long> {
}