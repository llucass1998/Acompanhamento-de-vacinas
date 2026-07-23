package com.lucas.vacinakids.importdata.dto;

import java.time.Instant;
import java.util.UUID;

public record ImportJobResponse(
    UUID id,
    UUID sourceId,
    String importType,
    String status,
    Instant startedAt,
    Instant finishedAt,
    Integer recordsFound,
    Integer recordsCreated,
    Integer recordsUpdated,
    Integer recordsRejected,
    String errorMessage,
    UUID executedBy,
    Instant createdAt
) {}
