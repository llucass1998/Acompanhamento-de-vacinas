package com.lucas.vacinakids.campaign.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CampaignRequest(
    @NotBlank(message = "Title is required")
    @Size(max = 160, message = "Title must not exceed 160 characters")
    String title,
    
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    String description,
    
    @Size(max = 500, message = "Target audience must not exceed 500 characters")
    String targetAudience,
    
    @NotNull(message = "Start date is required")
    LocalDate startDate,
    
    @NotNull(message = "End date is required")
    LocalDate endDate
) {}
