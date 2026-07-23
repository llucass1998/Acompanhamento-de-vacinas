package com.lucas.vacinakids.officialsource.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record OfficialSourceRequest(
    @NotBlank String sourceType,
    @NotBlank String title,
    @NotBlank String organization,
    String sourceUrl,
    @NotNull Integer referenceYear,
    Instant publishedAt,
    @NotBlank String contentHash,
    @NotBlank String status,
    String notes
) {}
