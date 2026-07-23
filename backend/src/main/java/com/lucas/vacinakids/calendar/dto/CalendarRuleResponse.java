package com.lucas.vacinakids.calendar.dto;

import java.time.Instant;
import java.util.UUID;

public record CalendarRuleResponse(
    UUID id,
    UUID calendarVersionId,
    UUID vaccineDoseId,
    String lifeStage,
    String audience,
    Integer recommendedAgeDays,
    Integer recommendedAgeMonths,
    String notes,
    boolean active,
    Instant createdAt,
    Instant updatedAt,
    Long versionNumber
) {}
