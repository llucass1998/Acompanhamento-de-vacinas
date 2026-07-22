package com.lucas.vacinakids.campaign.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CampaignRequest(
    @NotBlank(message = "Title is required")
    String title,
    
    String description,
    
    String targetAudience,
    
    @NotNull(message = "Start date is required")
    LocalDate startDate,
    
    @NotNull(message = "End date is required")
    LocalDate endDate
) {}
