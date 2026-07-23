package com.lucas.vacinakids.calendar.dto;

import java.time.LocalDate;
import java.time.Instant;
import java.util.UUID;

public record CalendarVersionResponse(
    UUID id,
    String name,
    Integer referenceYear,
    String version,
    String description,
    UUID sourceId,
    String status,
    LocalDate validFrom,
    LocalDate validUntil,
    Instant publishedAt,
    UUID publishedBy,
    Instant createdAt,
    Instant updatedAt,
    Long versionNumber
) {}
