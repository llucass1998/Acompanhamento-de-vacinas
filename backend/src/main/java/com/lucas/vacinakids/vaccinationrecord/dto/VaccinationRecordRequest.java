package com.lucas.vacinakids.vaccinationrecord.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.UUID;

public record VaccinationRecordRequest(
        @NotNull(message = "Dose ID is required")
        UUID doseId,
        
        @NotNull(message = "Applied date is required")
        @PastOrPresent(message = "Applied date cannot be in the future")
        LocalDate appliedDate,
        
        String location,
        String batchNumber,
        String observations
) {}
