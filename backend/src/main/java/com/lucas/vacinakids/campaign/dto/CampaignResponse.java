package com.lucas.vacinakids.campaign.dto;

import com.lucas.vacinakids.campaign.entity.Campaign;
import java.time.LocalDate;
import java.util.UUID;

public record CampaignResponse(
    UUID id,
    String title,
    String description,
    String targetAudience,
    LocalDate startDate,
    LocalDate endDate,
    boolean active,
    String status
) {
    public static CampaignResponse fromEntity(Campaign campaign, LocalDate referenceDate) {
        String status;
        if (!campaign.isActive()) {
            status = "INACTIVE";
        } else if (referenceDate.isBefore(campaign.getStartDate())) {
            status = "UPCOMING";
        } else if (referenceDate.isAfter(campaign.getEndDate())) {
            status = "FINISHED";
        } else {
            status = "ONGOING";
        }

        return new CampaignResponse(
            campaign.getId(),
            campaign.getTitle(),
            campaign.getDescription(),
            campaign.getTargetAudience(),
            campaign.getStartDate(),
            campaign.getEndDate(),
            campaign.isActive(),
            status
        );
    }
}
