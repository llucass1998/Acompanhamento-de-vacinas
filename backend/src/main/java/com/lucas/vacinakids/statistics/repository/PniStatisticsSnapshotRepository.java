package com.lucas.vacinakids.statistics.repository;

import com.lucas.vacinakids.statistics.entity.PniStatisticsSnapshot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PniStatisticsSnapshotRepository extends JpaRepository<PniStatisticsSnapshot, UUID> {
    Page<PniStatisticsSnapshot> findByReferenceYear(Integer referenceYear, Pageable pageable);
}
