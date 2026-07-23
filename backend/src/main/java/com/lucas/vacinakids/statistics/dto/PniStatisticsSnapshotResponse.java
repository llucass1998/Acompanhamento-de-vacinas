package com.lucas.vacinakids.statistics.dto;

import java.time.Instant;
import java.util.UUID;

public record PniStatisticsSnapshotResponse(
    UUID id,
    Integer referenceYear,
    Integer referenceMonth,
    String stateCode,
    String municipalityCode,
    String vaccineCode,
    String doseCode,
    String ageGroup,
    Long appliedDoses,
    UUID sourceId,
    Instant collectedAt
) {}
