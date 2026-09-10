package com.healthcare.analytics.dto;

public record DashboardSummary(
        long totalDoctors,
        long totalPatients,
        long totalDepartments,
        long totalAppointments
) {
}