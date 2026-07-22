package com.lucas.vacinakids.campaign.repository;

import com.lucas.vacinakids.campaign.entity.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, UUID> {

    @Query("SELECT c FROM Campaign c WHERE c.active = true AND c.startDate <= :date AND c.endDate >= :date ORDER BY c.endDate ASC")
    Page<Campaign> findActiveCampaignsAtDate(LocalDate date, Pageable pageable);

    @Query("SELECT c FROM Campaign c WHERE c.active = true AND c.endDate >= :date ORDER BY c.endDate ASC")
    Page<Campaign> findUpcomingOrActiveCampaigns(LocalDate date, Pageable pageable);
}
