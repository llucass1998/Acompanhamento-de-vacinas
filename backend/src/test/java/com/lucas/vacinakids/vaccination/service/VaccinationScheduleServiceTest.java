package com.lucas.vacinakids.vaccination.service;

import com.lucas.vacinakids.child.entity.Child;
import com.lucas.vacinakids.child.repository.ChildRepository;
import com.lucas.vacinakids.shared.exception.NotFoundException;
import com.lucas.vacinakids.vaccination.dto.VaccinationScheduleResponse;
import com.lucas.vacinakids.vaccination.dto.VaccinationSummaryResponse;
import com.lucas.vacinakids.vaccination.entity.VaccinationSchedule;
import com.lucas.vacinakids.vaccination.repository.VaccinationScheduleRepository;
import com.lucas.vacinakids.vaccine.entity.Vaccine;
import com.lucas.vacinakids.vaccine.entity.VaccineDose;
import com.lucas.vacinakids.vaccine.repository.VaccineDoseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VaccinationScheduleServiceTest {

    @Mock
    private VaccinationScheduleRepository scheduleRepository;
    @Mock
    private VaccineDoseRepository vaccineDoseRepository;
    @Mock
    private ChildRepository childRepository;

    @InjectMocks
    private VaccinationScheduleService scheduleService;

    private Child child;
    private VaccineDose bcgDose;
    private UUID childId;
    private UUID userId;
    private LocalDate today;

    @BeforeEach
    void setUp() {
        childId = UUID.randomUUID();
        userId = UUID.randomUUID();
        today = LocalDate.now();

        child = new Child(null, "Test Child", today.minusMonths(2), "Parent", "");
        child.setId(childId);

        Vaccine bcg = Vaccine.builder().name("BCG").description("Previne tuberculose").active(true).build();
        bcgDose = VaccineDose.builder().vaccine(bcg).description("Dose unica").recommendedAgeMonths(0).active(true).build();
    }

    @Test
    void generateScheduleForChild_ShouldCreateSchedule_WhenNotExists() {
        when(vaccineDoseRepository.findAllByActiveTrue()).thenReturn(List.of(bcgDose));
        when(scheduleRepository.existsByChildIdAndDoseId(childId, bcgDose.getId())).thenReturn(false);

        scheduleService.generateScheduleForChild(child);

        verify(scheduleRepository, times(1)).save(any(VaccinationSchedule.class));
    }

    @Test
    void generateScheduleForChild_ShouldNotCreateSchedule_WhenAlreadyExists() {
        when(vaccineDoseRepository.findAllByActiveTrue()).thenReturn(List.of(bcgDose));
        when(scheduleRepository.existsByChildIdAndDoseId(childId, bcgDose.getId())).thenReturn(true);

        scheduleService.generateScheduleForChild(child);

        verify(scheduleRepository, never()).save(any(VaccinationSchedule.class));
    }

    @Test
    void getChildSchedule_ShouldReturnSortedSchedules_WhenAccessAllowed() {
        when(childRepository.existsByIdAndUserIdAndActiveTrue(childId, userId)).thenReturn(true);
        
        VaccinationSchedule schedule = VaccinationSchedule.builder()
                .id(UUID.randomUUID())
                .child(child)
                .dose(bcgDose)
                .expectedDate(today.minusMonths(2))
                .status(VaccinationSchedule.VaccinationStatus.PENDING)
                .build();
                
        when(scheduleRepository.findByChildIdOrderByExpectedDateAsc(childId)).thenReturn(List.of(schedule));

        List<VaccinationScheduleResponse> responses = scheduleService.getChildSchedule(userId, childId);

        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        assertEquals(schedule.getId(), responses.get(0).id());
    }

    @Test
    void getChildSchedule_ShouldThrowNotFound_WhenAccessDenied() {
        when(childRepository.existsByIdAndUserIdAndActiveTrue(childId, userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> scheduleService.getChildSchedule(userId, childId));
    }

    @Test
    void getChildSummary_ShouldReturnCorrectMetrics_WhenAccessAllowed() {
        when(childRepository.existsByIdAndUserIdAndActiveTrue(childId, userId)).thenReturn(true);
        
        VaccinationSchedule pending = VaccinationSchedule.builder()
                .child(child).dose(bcgDose).expectedDate(today.plusDays(1)).status(VaccinationSchedule.VaccinationStatus.PENDING).build();
        VaccinationSchedule overdue = VaccinationSchedule.builder()
                .child(child).dose(bcgDose).expectedDate(today.minusDays(1)).status(VaccinationSchedule.VaccinationStatus.LATE).build();
        VaccinationSchedule applied = VaccinationSchedule.builder()
                .child(child).dose(bcgDose).expectedDate(today.minusDays(5)).status(VaccinationSchedule.VaccinationStatus.APPLIED).build();

        when(scheduleRepository.findByChildIdOrderByExpectedDateAsc(childId))
                .thenReturn(List.of(pending, overdue, applied));

        VaccinationSummaryResponse summary = scheduleService.getChildSummary(userId, childId);

        assertEquals(3, summary.total());
        assertEquals(1, summary.taken());
        assertEquals(1, summary.pending());
        assertEquals(1, summary.overdue());
        assertEquals(33.33333333333333, summary.completionPercentage());
    }
}
