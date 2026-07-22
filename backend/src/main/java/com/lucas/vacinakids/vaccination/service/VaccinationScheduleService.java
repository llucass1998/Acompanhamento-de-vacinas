package com.lucas.vacinakids.vaccination.service;

import com.lucas.vacinakids.child.entity.Child;
import com.lucas.vacinakids.child.repository.ChildRepository;
import com.lucas.vacinakids.shared.exception.NotFoundException;
import com.lucas.vacinakids.vaccination.dto.VaccinationScheduleResponse;
import com.lucas.vacinakids.vaccination.dto.VaccinationSummaryResponse;
import com.lucas.vacinakids.vaccination.entity.VaccinationSchedule;
import com.lucas.vacinakids.vaccination.repository.VaccinationScheduleRepository;
import com.lucas.vacinakids.vaccine.dto.VaccineDoseResponse;
import com.lucas.vacinakids.vaccine.entity.VaccineDose;
import com.lucas.vacinakids.vaccine.repository.VaccineDoseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VaccinationScheduleService {

    private final VaccinationScheduleRepository scheduleRepository;
    private final VaccineDoseRepository vaccineDoseRepository;
    private final ChildRepository childRepository;

    public VaccinationScheduleService(VaccinationScheduleRepository scheduleRepository,
                                      VaccineDoseRepository vaccineDoseRepository,
                                      ChildRepository childRepository) {
        this.scheduleRepository = scheduleRepository;
        this.vaccineDoseRepository = vaccineDoseRepository;
        this.childRepository = childRepository;
    }

    @Transactional
    public void generateScheduleForChild(Child child) {
        List<VaccineDose> activeDoses = vaccineDoseRepository.findAllByActiveTrue();
        
        for (VaccineDose dose : activeDoses) {
            if (!scheduleRepository.existsByChildIdAndDoseId(child.getId(), dose.getId())) {
                LocalDate expectedDate = child.getBirthDate().plusMonths(dose.getRecommendedAgeMonths());
                
                VaccinationSchedule schedule = VaccinationSchedule.builder()
                        .child(child)
                        .dose(dose)
                        .expectedDate(expectedDate)
                        .status(expectedDate.isBefore(LocalDate.now()) ? 
                                VaccinationSchedule.VaccinationStatus.LATE : 
                                VaccinationSchedule.VaccinationStatus.PENDING)
                        .build();
                scheduleRepository.save(schedule);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<VaccinationScheduleResponse> getChildSchedule(UUID userId, UUID childId) {
        validateChildAccess(userId, childId);
        
        return scheduleRepository.findByChildIdOrderByExpectedDateAsc(childId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VaccinationSummaryResponse getChildSummary(UUID userId, UUID childId) {
        validateChildAccess(userId, childId);

        List<VaccinationSchedule> schedules = scheduleRepository.findByChildIdOrderByExpectedDateAsc(childId);
        
        long total = schedules.size();
        
        long taken = schedules.stream()
                .filter(s -> s.getStatus() == VaccinationSchedule.VaccinationStatus.APPLIED)
                .count();
        
        long overdue = schedules.stream()
                .filter(s -> s.getStatus() == VaccinationSchedule.VaccinationStatus.LATE)
                .count();
                
        long pending = schedules.stream()
                .filter(s -> s.getStatus() == VaccinationSchedule.VaccinationStatus.PENDING)
                .count();

        double completionPercentage = total == 0 ? 0 : ((double) taken / total) * 100;

        return new VaccinationSummaryResponse(total, taken, pending, overdue, completionPercentage);
    }

    private void validateChildAccess(UUID userId, UUID childId) {
        if (!childRepository.existsByIdAndUserIdAndActiveTrue(childId, userId)) {
            throw new NotFoundException("Child not found or access denied");
        }
    }

    private VaccinationScheduleResponse mapToResponse(VaccinationSchedule schedule) {
        VaccineDose dose = schedule.getDose();
        VaccineDoseResponse doseResponse = new VaccineDoseResponse(
                dose.getId(),
                dose.getDescription(),
                dose.getRecommendedAgeMonths(),
                dose.getDescription(),
                "PNI",
                "2024"
        );

        return new VaccinationScheduleResponse(
                schedule.getId(),
                schedule.getChild().getId(),
                doseResponse,
                dose.getVaccine().getName(),
                schedule.getExpectedDate(),
                schedule.getStatus().name()
        );
    }
}
