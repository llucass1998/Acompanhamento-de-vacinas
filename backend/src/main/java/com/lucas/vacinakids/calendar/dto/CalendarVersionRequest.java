package com.lucas.vacinakids.calendar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record CalendarVersionRequest(
    @NotBlank String name,
    @NotNull Integer referenceYear,
    @NotBlank String version,
    String description,
    @NotNull UUID sourceId,
    @NotBlank String status,
    @NotNull LocalDate validFrom,
    LocalDate validUntil
) {}
