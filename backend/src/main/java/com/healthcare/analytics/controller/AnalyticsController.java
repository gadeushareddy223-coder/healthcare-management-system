package com.healthcare.analytics.controller;

import com.healthcare.analytics.dto.AppointmentAnalytics;
import com.healthcare.analytics.dto.DashboardSummary;
import com.healthcare.analytics.service.AnalyticsService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(
            AnalyticsService analyticsService
    ) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummary> getDashboardSummary() {

        DashboardSummary summary =
                analyticsService.getDashboardSummary();

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/appointments")
    public ResponseEntity<AppointmentAnalytics> getAppointmentAnalytics() {

        AppointmentAnalytics analytics =
                analyticsService.getAppointmentAnalytics();

        return ResponseEntity.ok(analytics);
    }
}