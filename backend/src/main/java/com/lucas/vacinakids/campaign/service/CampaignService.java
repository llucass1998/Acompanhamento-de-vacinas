package com.lucas.vacinakids.campaign.service;

import com.lucas.vacinakids.campaign.dto.CampaignRequest;
import com.lucas.vacinakids.campaign.dto.CampaignResponse;
import com.lucas.vacinakids.campaign.entity.Campaign;
import com.lucas.vacinakids.campaign.repository.CampaignRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lucas.vacinakids.shared.exception.BusinessException;
import com.lucas.vacinakids.shared.exception.NotFoundException;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;

    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @Transactional(readOnly = true)
    public Page<CampaignResponse> getActiveCampaigns(Pageable pageable) {
        LocalDate today = LocalDate.now();
        return campaignRepository.findActiveCampaignsAtDate(today, pageable)
                .map(campaign -> CampaignResponse.fromEntity(campaign, today));
    }

    @Transactional(readOnly = true)
    public Page<CampaignResponse> getUpcomingOrActiveCampaigns(Pageable pageable) {
        LocalDate today = LocalDate.now();
        return campaignRepository.findUpcomingOrActiveCampaigns(today, pageable)
                .map(campaign -> CampaignResponse.fromEntity(campaign, today));
    }

    @Transactional(readOnly = true)
    public Page<CampaignResponse> getAllCampaigns(Pageable pageable) {
        LocalDate today = LocalDate.now();
        return campaignRepository.findAll(pageable)
                .map(campaign -> CampaignResponse.fromEntity(campaign, today));
    }

    @Transactional(readOnly = true)
    public CampaignResponse getCampaignById(UUID id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Campaign not found"));
        return CampaignResponse.fromEntity(campaign, LocalDate.now());
    }

    @Transactional
    public CampaignResponse createCampaign(CampaignRequest request) {
        validateDates(request.startDate(), request.endDate());

        Campaign campaign = Campaign.builder()
                .title(request.title())
                .description(request.description())
                .targetAudience(request.targetAudience())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .active(true)
                .build();

        Campaign saved = campaignRepository.save(campaign);
        return CampaignResponse.fromEntity(saved, LocalDate.now());
    }

    @Transactional
    public CampaignResponse updateCampaign(UUID id, CampaignRequest request) {
        validateDates(request.startDate(), request.endDate());

        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Campaign not found"));

        campaign.setTitle(request.title());
        campaign.setDescription(request.description());
        campaign.setTargetAudience(request.targetAudience());
        campaign.setStartDate(request.startDate());
        campaign.setEndDate(request.endDate());

        Campaign saved = campaignRepository.save(campaign);
        return CampaignResponse.fromEntity(saved, LocalDate.now());
    }

    @Transactional
    public void deactivateCampaign(UUID id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Campaign not found"));
        
        campaign.setActive(false);
        campaignRepository.save(campaign);
    }
    
    @Transactional
    public void activateCampaign(UUID id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Campaign not found"));
        
        campaign.setActive(true);
        campaignRepository.save(campaign);
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new BusinessException("End date cannot be before start date");
        }
    }
}
