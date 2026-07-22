package com.lucas.vacinakids.child.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ChildUpdateRequest(
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 120, message = "Name must be between 2 and 120 characters")
    String name,

    @NotNull(message = "Birth date is required")
    @PastOrPresent(message = "Birth date cannot be in the future")
    LocalDate birthDate,

    @NotBlank(message = "Responsible name is required")
    @Size(min = 2, max = 120, message = "Responsible name must be between 2 and 120 characters")
    String responsibleName,

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    String notes
) {}
