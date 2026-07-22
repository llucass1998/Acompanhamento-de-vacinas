package com.lucas.vacinakids.vaccine.dto;

import java.util.UUID;

public record VaccineDoseResponse(
    UUID id,
    String doseName,
    Integer recommendedAgeMonths,
    String description,
    String source,
    String sourceVersion
) {}
