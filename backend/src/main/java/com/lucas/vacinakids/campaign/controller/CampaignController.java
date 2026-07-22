package com.lucas.vacinakids.campaign.controller;

import com.lucas.vacinakids.campaign.dto.CampaignResponse;
import com.lucas.vacinakids.campaign.service.CampaignService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Campaigns (Public)", description = "Visualização de campanhas para usuários comuns")
@RequestMapping("/api/v1/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping
    @Operation(summary = "Listar campanhas ativas e futuras")
    public ResponseEntity<Page<CampaignResponse>> getUpcomingOrActiveCampaigns(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(campaignService.getUpcomingOrActiveCampaigns(pageable));
    }
    
    @GetMapping("/active")
    @Operation(summary = "Listar apenas campanhas ativas no momento")
    public ResponseEntity<Page<CampaignResponse>> getActiveCampaigns(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(campaignService.getActiveCampaigns(pageable));
    }
}
