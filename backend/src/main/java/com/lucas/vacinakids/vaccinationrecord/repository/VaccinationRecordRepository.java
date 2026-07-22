package com.lucas.vacinakids.vaccinationrecord.repository;

import com.lucas.vacinakids.vaccinationrecord.entity.VaccinationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VaccinationRecordRepository extends JpaRepository<VaccinationRecord, UUID> {
    boolean existsByChildIdAndDoseId(UUID childId, UUID doseId);
    List<VaccinationRecord> findByChildIdOrderByAppliedDateDesc(UUID childId);
    Optional<VaccinationRecord> findByIdAndChildId(UUID id, UUID childId);
    long countByChildId(UUID childId);
}
