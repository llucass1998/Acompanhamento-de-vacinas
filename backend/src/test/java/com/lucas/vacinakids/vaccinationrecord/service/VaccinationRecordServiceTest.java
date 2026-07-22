package com.lucas.vacinakids.vaccinationrecord.service;

import com.lucas.vacinakids.child.entity.Child;
import com.lucas.vacinakids.child.repository.ChildRepository;
import com.lucas.vacinakids.shared.exception.BusinessException;
import com.lucas.vacinakids.shared.exception.NotFoundException;
import com.lucas.vacinakids.vaccination.entity.VaccinationSchedule;
import com.lucas.vacinakids.vaccination.repository.VaccinationScheduleRepository;
import com.lucas.vacinakids.vaccinationrecord.dto.VaccinationRecordRequest;
import com.lucas.vacinakids.vaccinationrecord.dto.VaccinationRecordResponse;
import com.lucas.vacinakids.vaccinationrecord.entity.VaccinationRecord;
import com.lucas.vacinakids.vaccinationrecord.repository.VaccinationRecordRepository;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VaccinationRecordServiceTest {

    @Mock
    private VaccinationRecordRepository recordRepository;

    @Mock
    private ChildRepository childRepository;

    @Mock
    private VaccineDoseRepository doseRepository;

    @Mock
    private VaccinationScheduleRepository scheduleRepository;

    @InjectMocks
    private VaccinationRecordService recordService;

    private UUID userId;
    private UUID childId;
    private UUID doseId;
    private UUID recordId;
    private Child child;
    private VaccineDose dose;
    private VaccinationRecord record;
    private VaccinationSchedule schedule;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        childId = UUID.randomUUID();
        doseId = UUID.randomUUID();
        recordId = UUID.randomUUID();

        child = new Child(null, "Test Child", LocalDate.now().minusYears(1), "Parent", "");
        child.setId(childId);
        
        Vaccine vaccine = Vaccine.builder().id(UUID.randomUUID()).name("BCG").build();
        dose = VaccineDose.builder().id(doseId).vaccine(vaccine).description("Unique Dose").build();
        
        record = VaccinationRecord.builder().id(recordId).child(child).dose(dose).appliedDate(LocalDate.now()).build();
        
        schedule = VaccinationSchedule.builder()
                .id(UUID.randomUUID())
                .child(child)
                .dose(dose)
                .expectedDate(LocalDate.now())
                .status(VaccinationSchedule.VaccinationStatus.PENDING)
                .build();
    }

    @Test
    void registerDose_withValidData_shouldSaveAndReturnResponse() {
        VaccinationRecordRequest request = new VaccinationRecordRequest(doseId, LocalDate.now(), "Posto 1", "Lote X", "Obs");

        when(childRepository.findByIdAndUserIdAndActiveTrue(childId, userId)).thenReturn(Optional.of(child));
        when(doseRepository.findByIdAndActiveTrue(doseId)).thenReturn(Optional.of(dose));
        when(recordRepository.existsByChildIdAndDoseId(childId, doseId)).thenReturn(false);
        when(recordRepository.save(any(VaccinationRecord.class))).thenReturn(record);
        when(scheduleRepository.findByChildIdAndDoseId(childId, doseId)).thenReturn(Optional.of(schedule));

        VaccinationRecordResponse response = recordService.registerDose(userId, childId, request);

        assertNotNull(response);
        assertEquals(recordId, response.id());
        assertEquals("BCG", response.vaccineName());
        
        verify(recordRepository).save(any(VaccinationRecord.class));
        verify(scheduleRepository).save(schedule);
        assertEquals(VaccinationSchedule.VaccinationStatus.APPLIED, schedule.getStatus());
    }

    @Test
    void registerDose_withDuplicateRecord_shouldThrowBusinessException() {
        VaccinationRecordRequest request = new VaccinationRecordRequest(doseId, LocalDate.now(), null, null, null);

        when(childRepository.findByIdAndUserIdAndActiveTrue(childId, userId)).thenReturn(Optional.of(child));
        when(doseRepository.findByIdAndActiveTrue(doseId)).thenReturn(Optional.of(dose));
        when(recordRepository.existsByChildIdAndDoseId(childId, doseId)).thenReturn(true);

        assertThrows(BusinessException.class, () -> recordService.registerDose(userId, childId, request));
        verify(recordRepository, never()).save(any());
    }

    @Test
    void getChildHistory_withValidAccess_shouldReturnList() {
        when(childRepository.findByIdAndUserIdAndActiveTrue(childId, userId)).thenReturn(Optional.of(child));
        when(recordRepository.findByChildIdOrderByAppliedDateDesc(childId)).thenReturn(List.of(record));

        List<VaccinationRecordResponse> history = recordService.getChildHistory(userId, childId);

        assertFalse(history.isEmpty());
        assertEquals(1, history.size());
        assertEquals(recordId, history.get(0).id());
    }

    @Test
    void getChildHistory_withInvalidAccess_shouldThrowNotFoundException() {
        when(childRepository.findByIdAndUserIdAndActiveTrue(childId, userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> recordService.getChildHistory(userId, childId));
    }
    
    @Test
    void deleteRecord_withValidData_shouldDeleteAndRevertSchedule() {
        when(childRepository.findByIdAndUserIdAndActiveTrue(childId, userId)).thenReturn(Optional.of(child));
        when(recordRepository.findByIdAndChildId(recordId, childId)).thenReturn(Optional.of(record));
        
        schedule.setStatus(VaccinationSchedule.VaccinationStatus.APPLIED);
        when(scheduleRepository.findByChildIdAndDoseId(childId, doseId)).thenReturn(Optional.of(schedule));
        
        recordService.deleteRecord(userId, childId, recordId);
        
        verify(recordRepository).delete(record);
        verify(scheduleRepository).save(schedule);
        assertEquals(VaccinationSchedule.VaccinationStatus.PENDING, schedule.getStatus());
        assertNull(schedule.getAppliedDate());
    }
}
