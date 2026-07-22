package com.lucas.vacinakids.campaign.service;

import com.lucas.vacinakids.campaign.dto.CampaignRequest;
import com.lucas.vacinakids.campaign.dto.CampaignResponse;
import com.lucas.vacinakids.campaign.entity.Campaign;
import com.lucas.vacinakids.campaign.repository.CampaignRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.lucas.vacinakids.shared.exception.BusinessException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampaignServiceTest {

    @Mock
    private CampaignRepository campaignRepository;

    @InjectMocks
    private CampaignService campaignService;

    private Campaign testCampaign;
    private UUID campaignId;

    @BeforeEach
    void setUp() {
        campaignId = UUID.randomUUID();
        testCampaign = new Campaign(
                campaignId,
                "Campanha de Teste",
                "Descricao da campanha",
                "Criancas de 1 a 5 anos",
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(10),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void getActiveCampaigns_shouldReturnPageOfCampaigns() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Campaign> campaignPage = new PageImpl<>(List.of(testCampaign));
        
        when(campaignRepository.findActiveCampaignsAtDate(any(LocalDate.class), eq(pageable)))
                .thenReturn(campaignPage);

        Page<CampaignResponse> result = campaignService.getActiveCampaigns(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Campanha de Teste", result.getContent().get(0).title());
        assertEquals("ONGOING", result.getContent().get(0).status());
    }

    @Test
    void createCampaign_withValidDates_shouldSaveAndReturnCampaign() {
        CampaignRequest request = new CampaignRequest(
                "Nova Campanha",
                "Descricao",
                "Publico",
                LocalDate.now(),
                LocalDate.now().plusDays(5)
        );

        when(campaignRepository.save(any(Campaign.class))).thenReturn(testCampaign);

        CampaignResponse result = campaignService.createCampaign(request);

        assertNotNull(result);
        verify(campaignRepository, times(1)).save(any(Campaign.class));
    }

    @Test
    void createCampaign_withInvalidDates_shouldThrowException() {
        CampaignRequest request = new CampaignRequest(
                "Nova Campanha",
                "Descricao",
                "Publico",
                LocalDate.now().plusDays(5),
                LocalDate.now()
        );

        assertThrows(BusinessException.class, () -> campaignService.createCampaign(request));
        verify(campaignRepository, never()).save(any(Campaign.class));
    }

    @Test
    void deactivateCampaign_shouldSetCampaignToInactive() {
        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(testCampaign));
        
        campaignService.deactivateCampaign(campaignId);
        
        assertFalse(testCampaign.isActive());
        verify(campaignRepository, times(1)).save(testCampaign);
    }
}
