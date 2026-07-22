package com.lucas.vacinakids.vaccine.service;

import com.lucas.vacinakids.shared.exception.NotFoundException;
import com.lucas.vacinakids.vaccine.dto.VaccineResponse;
import com.lucas.vacinakids.vaccine.entity.Vaccine;
import com.lucas.vacinakids.vaccine.entity.VaccineDose;
import com.lucas.vacinakids.vaccine.repository.VaccineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VaccineServiceTest {

    @Mock
    private VaccineRepository vaccineRepository;

    @InjectMocks
    private VaccineService vaccineService;

    private Vaccine vaccine;
    private VaccineDose dose;

    @BeforeEach
    void setUp() {
        vaccine = Vaccine.builder()
                .id(UUID.randomUUID())
                .name("BCG")
                .description("Previne tuberculose")
                .active(true)
                .doses(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        dose = VaccineDose.builder()
                .id(UUID.randomUUID())
                .description("Dose única")
                .recommendedAgeMonths(0)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
                
        vaccine.addDose(dose);
    }

    @Test
    void getAllVaccines_ShouldReturnMappedResponses() {
        when(vaccineRepository.findAllByActiveTrueOrderByNameAsc()).thenReturn(List.of(vaccine));

        List<VaccineResponse> responses = vaccineService.getAllVaccines();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("BCG", responses.get(0).name());
        assertEquals(1, responses.get(0).doses().size());
        assertEquals("Dose única", responses.get(0).doses().get(0).doseName());
    }

    @Test
    void getVaccineById_ShouldReturnMappedResponse_WhenExists() {
        when(vaccineRepository.findByIdAndActiveTrue(vaccine.getId())).thenReturn(Optional.of(vaccine));

        VaccineResponse response = vaccineService.getVaccineById(vaccine.getId());

        assertNotNull(response);
        assertEquals("BCG", response.name());
        assertEquals(1, response.doses().size());
    }

    @Test
    void getVaccineById_ShouldThrowNotFound_WhenNotExists() {
        UUID randomId = UUID.randomUUID();
        when(vaccineRepository.findByIdAndActiveTrue(randomId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> vaccineService.getVaccineById(randomId));
    }
}
