package com.healthcare.medicalhistory.repository;

import com.healthcare.medicalhistory.MedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalHistoryRepository
        extends JpaRepository<MedicalHistory, Long> {
}