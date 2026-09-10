package com.healthcare.analytics.service;

import com.healthcare.analytics.dto.AppointmentAnalytics;
import com.healthcare.analytics.dto.DashboardSummary;
import com.healthcare.appointment.repository.AppointmentRepository;
import com.healthcare.department.repository.DepartmentRepository;
import com.healthcare.doctor.repository.DoctorRepository;
import com.healthcare.patient.repository.PatientRepository;

import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DepartmentRepository departmentRepository;
    private final AppointmentRepository appointmentRepository;

    public AnalyticsService(
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            DepartmentRepository departmentRepository,
            AppointmentRepository appointmentRepository
    ) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.departmentRepository = departmentRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public DashboardSummary getDashboardSummary() {

        long totalDoctors = doctorRepository.count();

        long totalPatients = patientRepository.count();

        long totalDepartments = departmentRepository.count();

        long totalAppointments = appointmentRepository.count();

        return new DashboardSummary(
                totalDoctors,
                totalPatients,
                totalDepartments,
                totalAppointments
        );
    }

    public AppointmentAnalytics getAppointmentAnalytics() {

        long total = appointmentRepository.count();

        long scheduled =
                appointmentRepository.countByStatus("SCHEDULED");

        long completed =
                appointmentRepository.countByStatus("COMPLETED");

        long cancelled =
                appointmentRepository.countByStatus("CANCELLED");

        long pending =
                appointmentRepository.countByStatus("PENDING");

        return new AppointmentAnalytics(
                total,
                scheduled,
                completed,
                cancelled,
                pending
        );
    }
}