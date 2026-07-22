package com.lucas.vacinakids.vaccine.service;

import com.lucas.vacinakids.shared.exception.NotFoundException;
import com.lucas.vacinakids.vaccine.dto.VaccineDoseResponse;
import com.lucas.vacinakids.vaccine.dto.VaccineResponse;
import com.lucas.vacinakids.vaccine.entity.Vaccine;
import com.lucas.vacinakids.vaccine.entity.VaccineDose;
import com.lucas.vacinakids.vaccine.repository.VaccineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VaccineService {

    private final VaccineRepository vaccineRepository;

    public VaccineService(VaccineRepository vaccineRepository) {
        this.vaccineRepository = vaccineRepository;
    }

    @Transactional(readOnly = true)
    public List<VaccineResponse> getAllVaccines() {
        return vaccineRepository.findAllByActiveTrueOrderByNameAsc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VaccineResponse getVaccineById(UUID id) {
        Vaccine vaccine = vaccineRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Vaccine not found"));
        return mapToResponse(vaccine);
    }

    private VaccineResponse mapToResponse(Vaccine vaccine) {
        List<VaccineDoseResponse> doses = vaccine.getDoses().stream()
                .filter(VaccineDose::isActive)
                .map(d -> new VaccineDoseResponse(
                        d.getId(),
                        d.getDescription(),
                        d.getRecommendedAgeMonths(),
                        d.getDescription(),
                        "PNI",
                        "2024"
                ))
                .collect(Collectors.toList());

        return new VaccineResponse(
                vaccine.getId(),
                vaccine.getName(),
                vaccine.getDescription(),
                doses
        );
    }
}
