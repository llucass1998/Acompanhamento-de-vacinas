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
import com.lucas.vacinakids.vaccine.entity.VaccineDose;
import com.lucas.vacinakids.vaccine.repository.VaccineDoseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VaccinationRecordService {

    private final VaccinationRecordRepository recordRepository;
    private final ChildRepository childRepository;
    private final VaccineDoseRepository doseRepository;
    private final VaccinationScheduleRepository scheduleRepository;

    public VaccinationRecordService(VaccinationRecordRepository recordRepository,
                                  ChildRepository childRepository,
                                  VaccineDoseRepository doseRepository,
                                  VaccinationScheduleRepository scheduleRepository) {
        this.recordRepository = recordRepository;
        this.childRepository = childRepository;
        this.doseRepository = doseRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional
    public VaccinationRecordResponse registerDose(UUID userId, UUID childId, VaccinationRecordRequest request) {
        Child child = validateChildAccess(userId, childId);
        
        VaccineDose dose = doseRepository.findByIdAndActiveTrue(request.doseId())
                .orElseThrow(() -> new NotFoundException("Active vaccine dose not found"));

        if (recordRepository.existsByChildIdAndDoseId(childId, dose.getId())) {
            throw new BusinessException("This dose has already been registered for this child");
        }

        VaccinationRecord record = VaccinationRecord.builder()
                .child(child)
                .dose(dose)
                .appliedDate(request.appliedDate())
                .location(request.location())
                .batchNumber(request.batchNumber())
                .observations(request.observations())
                .build();

        record = recordRepository.save(record);

        // Update schedule if exists
        Optional<VaccinationSchedule> scheduleOpt = scheduleRepository.findByChildIdAndDoseId(childId, dose.getId());
        if (scheduleOpt.isPresent()) {
            VaccinationSchedule schedule = scheduleOpt.get();
            schedule.setStatus(VaccinationSchedule.VaccinationStatus.APPLIED);
            schedule.setAppliedDate(request.appliedDate());
            scheduleRepository.save(schedule);
        }

        return mapToResponse(record);
    }

    @Transactional(readOnly = true)
    public List<VaccinationRecordResponse> getChildHistory(UUID userId, UUID childId) {
        validateChildAccess(userId, childId);

        return recordRepository.findByChildIdOrderByAppliedDateDesc(childId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteRecord(UUID userId, UUID childId, UUID recordId) {
        validateChildAccess(userId, childId);

        VaccinationRecord record = recordRepository.findByIdAndChildId(recordId, childId)
                .orElseThrow(() -> new NotFoundException("Record not found"));

        // Revert schedule status
        Optional<VaccinationSchedule> scheduleOpt = scheduleRepository.findByChildIdAndDoseId(childId, record.getDose().getId());
        if (scheduleOpt.isPresent()) {
            VaccinationSchedule schedule = scheduleOpt.get();
            schedule.setAppliedDate(null);
            
            if (schedule.getExpectedDate().isBefore(LocalDate.now())) {
                schedule.setStatus(VaccinationSchedule.VaccinationStatus.LATE);
            } else {
                schedule.setStatus(VaccinationSchedule.VaccinationStatus.PENDING);
            }
            scheduleRepository.save(schedule);
        }

        recordRepository.delete(record);
    }

    private Child validateChildAccess(UUID userId, UUID childId) {
        return childRepository.findByIdAndUserIdAndActiveTrue(childId, userId)
                .orElseThrow(() -> new NotFoundException("Child not found or access denied"));
    }

    private VaccinationRecordResponse mapToResponse(VaccinationRecord record) {
        return new VaccinationRecordResponse(
                record.getId(),
                record.getChild().getId(),
                record.getDose().getId(),
                record.getDose().getVaccine().getName(),
                record.getDose().getDescription(), // Use description as dose name since we mapped it earlier
                record.getAppliedDate(),
                record.getLocation(),
                record.getBatchNumber(),
                record.getObservations(),
                record.getProofUrl()
        );
    }
}
