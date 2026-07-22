package com.lucas.vacinakids.vaccine.repository;

import com.lucas.vacinakids.vaccine.entity.VaccineDose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VaccineDoseRepository extends JpaRepository<VaccineDose, UUID> {
    List<VaccineDose> findByVaccineIdAndActiveTrueOrderByRecommendedAgeMonthsAsc(UUID vaccineId);
    List<VaccineDose> findAllByActiveTrue();
    java.util.Optional<VaccineDose> findByIdAndActiveTrue(UUID id);
}
