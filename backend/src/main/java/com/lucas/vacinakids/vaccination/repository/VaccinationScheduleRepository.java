package com.lucas.vacinakids.vaccination.repository;

import com.lucas.vacinakids.vaccination.entity.VaccinationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VaccinationScheduleRepository extends JpaRepository<VaccinationSchedule, UUID> {
    List<VaccinationSchedule> findByChildIdOrderByExpectedDateAsc(UUID childId);
    Optional<VaccinationSchedule> findByChildIdAndDoseId(UUID childId, UUID vaccineDoseId);
    boolean existsByChildIdAndDoseId(UUID childId, UUID vaccineDoseId);
}
