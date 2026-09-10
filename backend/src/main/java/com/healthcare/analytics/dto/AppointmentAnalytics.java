package com.healthcare.analytics.dto;

public record AppointmentAnalytics(
        long total,
        long scheduled,
        long completed,
        long cancelled,
        long pending
) {
}