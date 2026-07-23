package com.lucas.vacinakids.officialsource.dto;

import java.time.Instant;
import java.util.UUID;

public record OfficialSourceResponse(
    UUID id,
    String sourceType,
    String title,
    String organization,
    String sourceUrl,
    Integer referenceYear,
    Instant publishedAt,
    Instant retrievedAt,
    String contentHash,
    String status,
    String notes,
    Instant createdAt,
    Instant updatedAt
) {}
