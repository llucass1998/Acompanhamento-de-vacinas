package com.lucas.vacinakids.vaccinationrecord.dto;

import java.time.LocalDate;
import java.util.UUID;

public record VaccinationRecordResponse(
        UUID id,
        UUID childId,
        UUID doseId,
        String vaccineName,
        String doseName,
        LocalDate appliedDate,
        String location,
        String batchNumber,
        String observations,
        String proofUrl
) {}
