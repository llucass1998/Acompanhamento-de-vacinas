package com.lucas.vacinakids.calendar.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.vacinakids.calendar.dto.CalendarVersionRequest;
import com.lucas.vacinakids.calendar.dto.CalendarVersionResponse;
import com.lucas.vacinakids.calendar.entity.CalendarVersion;
import com.lucas.vacinakids.calendar.repository.CalendarVersionRepository;
import com.lucas.vacinakids.officialsource.entity.OfficialSource;
import com.lucas.vacinakids.officialsource.repository.OfficialSourceRepository;
import com.lucas.vacinakids.shared.exception.NotFoundException;
import com.lucas.vacinakids.user.entity.User;
import com.lucas.vacinakids.user.repository.UserRepository;
import com.lucas.vacinakids.user.service.AuditService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CalendarVersionService {

    private final CalendarVersionRepository repository;
    private final OfficialSourceRepository sourceRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    public CalendarVersionService(CalendarVersionRepository repository, OfficialSourceRepository sourceRepository, UserRepository userRepository, AuditService auditService, ObjectMapper objectMapper) {
        this.repository = repository;
        this.sourceRepository = sourceRepository;
        this.userRepository = userRepository;
        this.auditService = auditService;
        this.objectMapper = objectMapper;
    }

    public Page<CalendarVersionResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    public CalendarVersionResponse findById(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Calendar version not found"));
    }

    @Transactional
    public CalendarVersionResponse create(UUID adminId, CalendarVersionRequest request) {
        OfficialSource source = sourceRepository.findById(request.sourceId())
                .orElseThrow(() -> new NotFoundException("Official source not found"));
        
        CalendarVersion version = new CalendarVersion();
        version.setName(request.name());
        version.setReferenceYear(request.referenceYear());
        version.setVersion(request.version());
        version.setDescription(request.description());
        version.setSource(source);
        version.setStatus(request.status());
        version.setValidFrom(request.validFrom());
        version.setValidUntil(request.validUntil());

        version = repository.save(version);

        try {
            String afterData = objectMapper.writeValueAsString(request);
            auditService.logAdminAction(adminId, "CREATE_CALENDAR_VERSION", "CalendarVersion", version.getId(), null, afterData, "Manual creation");
        } catch (JsonProcessingException ignored) {}

        return toResponse(version);
    }

    private CalendarVersionResponse toResponse(CalendarVersion v) {
        return new CalendarVersionResponse(
            v.getId(), v.getName(), v.getReferenceYear(), v.getVersion(), v.getDescription(),
            v.getSource().getId(), v.getStatus(), v.getValidFrom(), v.getValidUntil(),
            v.getPublishedAt(), v.getPublishedBy() != null ? v.getPublishedBy().getId() : null,
            v.getCreatedAt(), v.getUpdatedAt(), v.getVersionNumber()
        );
    }
}
