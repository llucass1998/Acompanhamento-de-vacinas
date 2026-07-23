package com.lucas.vacinakids.statistics.service;

import com.lucas.vacinakids.statistics.dto.PniStatisticsSnapshotResponse;
import com.lucas.vacinakids.statistics.entity.PniStatisticsSnapshot;
import com.lucas.vacinakids.statistics.repository.PniStatisticsSnapshotRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PniStatisticsSnapshotService {

    private final PniStatisticsSnapshotRepository repository;

    public PniStatisticsSnapshotService(PniStatisticsSnapshotRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<PniStatisticsSnapshotResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<PniStatisticsSnapshotResponse> findByYear(Integer year, Pageable pageable) {
        return repository.findByReferenceYear(year, pageable).map(this::toResponse);
    }

    private PniStatisticsSnapshotResponse toResponse(PniStatisticsSnapshot s) {
        return new PniStatisticsSnapshotResponse(
            s.getId(),
            s.getReferenceYear(),
            s.getReferenceMonth(),
            s.getStateCode(),
            s.getMunicipalityCode(),
            s.getVaccineCode(),
            s.getDoseCode(),
            s.getAgeGroup(),
            s.getAppliedDoses(),
            s.getSource() != null ? s.getSource().getId() : null,
            s.getCollectedAt()
        );
    }
}
