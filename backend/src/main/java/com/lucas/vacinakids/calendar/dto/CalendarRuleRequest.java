package com.lucas.vacinakids.calendar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CalendarRuleRequest(
    @NotNull UUID calendarVersionId,
    @NotNull UUID vaccineDoseId,
    @NotBlank String lifeStage,
    @NotBlank String audience,
    Integer recommendedAgeDays,
    Integer recommendedAgeMonths,
    String notes,
    @NotNull Boolean active
) {}
