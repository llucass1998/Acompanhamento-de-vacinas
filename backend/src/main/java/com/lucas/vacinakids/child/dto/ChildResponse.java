package com.lucas.vacinakids.child.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ChildResponse(
    UUID id,
    String name,
    LocalDate birthDate,
    String responsibleName,
    String notes,
    Instant createdAt,
    Instant updatedAt
) {}
