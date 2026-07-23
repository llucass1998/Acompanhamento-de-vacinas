package com.lucas.vacinakids.importdata.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record ImportJobRequest(
    UUID sourceId,
    @NotBlank String importType
) {}
