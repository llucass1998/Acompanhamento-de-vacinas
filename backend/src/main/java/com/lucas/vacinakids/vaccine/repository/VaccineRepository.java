package com.lucas.vacinakids.vaccine.repository;

import com.lucas.vacinakids.vaccine.entity.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, UUID> {
    List<Vaccine> findAllByActiveTrueOrderByNameAsc();
    java.util.Optional<Vaccine> findByIdAndActiveTrue(UUID id);
}
