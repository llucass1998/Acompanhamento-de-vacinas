package com.lucas.vacinakids.campaign.controller;

import com.lucas.vacinakids.campaign.dto.CampaignRequest;
import com.lucas.vacinakids.campaign.dto.CampaignResponse;
import com.lucas.vacinakids.campaign.service.CampaignService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(name = "Campaigns (Admin)", description = "Gerenciamento de campanhas por administradores")
@RequestMapping("/api/v1/admin/campaigns")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCampaignController {

    private final CampaignService campaignService;

    public AdminCampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as campanhas (incluindo inativas)")
    public ResponseEntity<Page<CampaignResponse>> getAllCampaigns(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(campaignService.getAllCampaigns(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar campanha por ID")
    public ResponseEntity<CampaignResponse> getCampaignById(@PathVariable UUID id) {
        return ResponseEntity.ok(campaignService.getCampaignById(id));
    }

    @PostMapping
    @Operation(summary = "Criar nova campanha")
    public ResponseEntity<CampaignResponse> createCampaign(@Valid @RequestBody CampaignRequest request) {
        CampaignResponse response = campaignService.createCampaign(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar campanha existente")
    public ResponseEntity<CampaignResponse> updateCampaign(
            @PathVariable UUID id, 
            @Valid @RequestBody CampaignRequest request) {
        return ResponseEntity.ok(campaignService.updateCampaign(id, request));
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Desativar campanha")
    public ResponseEntity<Void> deactivateCampaign(@PathVariable UUID id) {
        campaignService.deactivateCampaign(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}/activate")
    @Operation(summary = "Reativar campanha")
    public ResponseEntity<Void> activateCampaign(@PathVariable UUID id) {
        campaignService.activateCampaign(id);
        return ResponseEntity.noContent().build();
    }
}
