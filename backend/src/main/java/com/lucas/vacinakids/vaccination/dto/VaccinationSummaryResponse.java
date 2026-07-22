package com.lucas.vacinakids.vaccination.dto;

public record VaccinationSummaryResponse(
    long total,
    long taken,
    long pending,
    long overdue,
    double completionPercentage
) {}
