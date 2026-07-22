package com.lucas.vacinakids.vaccination.dto;

import com.lucas.vacinakids.vaccine.dto.VaccineDoseResponse;

import java.time.LocalDate;
import java.util.UUID;

public record VaccinationScheduleResponse(
    UUID id,
    UUID childId,
    VaccineDoseResponse vaccineDose,
    String vaccineName,
    LocalDate expectedDate,
    String status // TAKEN, OVERDUE, PENDING
) {}
