package com.lucas.vacinakids.vaccinationrecord.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

public record VaccinationRecordRequest(
        @NotNull(message = "Dose ID is required")
        UUID doseId,
        
        @NotNull(message = "Applied date is required")
        @PastOrPresent(message = "Applied date cannot be in the future")
        LocalDate appliedDate,
        
        @Size(max = 160, message = "Location must not exceed 160 characters") String location,
        @Size(max = 120, message = "Batch number must not exceed 120 characters") String batchNumber,
        @Size(max = 500, message = "Observations must not exceed 500 characters") String observations
) {}
