package com.lucas.vacinakids.calendar.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.vacinakids.calendar.dto.CalendarRuleRequest;
import com.lucas.vacinakids.calendar.dto.CalendarRuleResponse;
import com.lucas.vacinakids.calendar.entity.CalendarRule;
import com.lucas.vacinakids.calendar.entity.CalendarVersion;
import com.lucas.vacinakids.calendar.repository.CalendarRuleRepository;
import com.lucas.vacinakids.calendar.repository.CalendarVersionRepository;
import com.lucas.vacinakids.vaccine.entity.VaccineDose;
import com.lucas.vacinakids.vaccine.repository.VaccineDoseRepository;
import com.lucas.vacinakids.shared.exception.NotFoundException;
import com.lucas.vacinakids.user.service.AuditService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CalendarRuleService {

    private final CalendarRuleRepository repository;
    private final CalendarVersionRepository versionRepository;
    private final VaccineDoseRepository doseRepository;
    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    public CalendarRuleService(CalendarRuleRepository repository, CalendarVersionRepository versionRepository, 
                               VaccineDoseRepository doseRepository, AuditService auditService, ObjectMapper objectMapper) {
        this.repository = repository;
        this.versionRepository = versionRepository;
        this.doseRepository = doseRepository;
        this.auditService = auditService;
        this.objectMapper = objectMapper;
    }

    public Page<CalendarRuleResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    public CalendarRuleResponse findById(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Calendar rule not found"));
    }

    @Transactional
    public CalendarRuleResponse create(UUID adminId, CalendarRuleRequest request) {
        CalendarVersion version = versionRepository.findById(request.calendarVersionId())
                .orElseThrow(() -> new NotFoundException("Calendar version not found"));
        
        VaccineDose dose = doseRepository.findById(request.vaccineDoseId())
                .orElseThrow(() -> new NotFoundException("Vaccine dose not found"));
        
        CalendarRule rule = new CalendarRule();
        rule.setCalendarVersion(version);
        rule.setVaccineDose(dose);
        rule.setLifeStage(request.lifeStage());
        rule.setAudience(request.audience());
        rule.setRecommendedAgeDays(request.recommendedAgeDays());
        rule.setRecommendedAgeMonths(request.recommendedAgeMonths());
        rule.setNotes(request.notes());
        rule.setActive(request.active());
        rule.setSource(version.getSource()); // Inherit source from the CalendarVersion

        rule = repository.save(rule);

        try {
            String afterData = objectMapper.writeValueAsString(request);
            auditService.logAdminAction(adminId, "CREATE_CALENDAR_RULE", "CalendarRule", rule.getId(), null, afterData, "Manual creation");
        } catch (JsonProcessingException ignored) {}

        return toResponse(rule);
    }

    private CalendarRuleResponse toResponse(CalendarRule r) {
        return new CalendarRuleResponse(
            r.getId(), r.getCalendarVersion().getId(), r.getVaccineDose().getId(), 
            r.getLifeStage(), r.getAudience(), r.getRecommendedAgeDays(), r.getRecommendedAgeMonths(),
            r.getNotes(), r.isActive(), r.getCreatedAt(), r.getUpdatedAt(), r.getVersionNumber()
        );
    }
}
