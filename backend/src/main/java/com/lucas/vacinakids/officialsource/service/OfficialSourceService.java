package com.lucas.vacinakids.officialsource.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.vacinakids.officialsource.dto.OfficialSourceRequest;
import com.lucas.vacinakids.officialsource.dto.OfficialSourceResponse;
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

import java.time.Instant;
import java.util.UUID;

@Service
public class OfficialSourceService {

    private final OfficialSourceRepository repository;
    private final UserRepository userRepository;
    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    public OfficialSourceService(OfficialSourceRepository repository, UserRepository userRepository, AuditService auditService, ObjectMapper objectMapper) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.auditService = auditService;
        this.objectMapper = objectMapper;
    }

    public Page<OfficialSourceResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    public OfficialSourceResponse findById(UUID id) {
        OfficialSource source = repository.findById(id).orElseThrow(() -> new NotFoundException("Official source not found"));
        return toResponse(source);
    }

    @Transactional
    public OfficialSourceResponse create(UUID adminId, OfficialSourceRequest request) {
        User admin = userRepository.findById(adminId).orElseThrow(() -> new NotFoundException("Admin not found"));
        
        OfficialSource source = new OfficialSource();
        source.setSourceType(request.sourceType());
        source.setTitle(request.title());
        source.setOrganization(request.organization());
        source.setSourceUrl(request.sourceUrl());
        source.setReferenceYear(request.referenceYear());
        source.setPublishedAt(request.publishedAt());
        source.setRetrievedAt(Instant.now());
        source.setContentHash(request.contentHash());
        source.setStatus(request.status());
        source.setNotes(request.notes());
        source.setCreatedBy(admin);

        source = repository.save(source);

        try {
            String afterData = objectMapper.writeValueAsString(request);
            auditService.logAdminAction(adminId, "CREATE_OFFICIAL_SOURCE", "OfficialSource", source.getId(), null, afterData, "Manual creation");
        } catch (JsonProcessingException ignored) {}

        return toResponse(source);
    }

    @Transactional
    public OfficialSourceResponse update(UUID adminId, UUID id, OfficialSourceRequest request) {
        OfficialSource source = repository.findById(id).orElseThrow(() -> new NotFoundException("Official source not found"));
        
        String beforeData = null;
        try {
            beforeData = objectMapper.writeValueAsString(toResponse(source));
        } catch (JsonProcessingException ignored) {}

        source.setSourceType(request.sourceType());
        source.setTitle(request.title());
        source.setOrganization(request.organization());
        source.setSourceUrl(request.sourceUrl());
        source.setReferenceYear(request.referenceYear());
        source.setPublishedAt(request.publishedAt());
        source.setContentHash(request.contentHash());
        source.setStatus(request.status());
        source.setNotes(request.notes());

        source = repository.save(source);

        try {
            String afterData = objectMapper.writeValueAsString(request);
            auditService.logAdminAction(adminId, "UPDATE_OFFICIAL_SOURCE", "OfficialSource", source.getId(), beforeData, afterData, "Manual update");
        } catch (JsonProcessingException ignored) {}

        return toResponse(source);
    }

    private OfficialSourceResponse toResponse(OfficialSource s) {
        return new OfficialSourceResponse(
                s.getId(), s.getSourceType(), s.getTitle(), s.getOrganization(), s.getSourceUrl(),
                s.getReferenceYear(), s.getPublishedAt(), s.getRetrievedAt(), s.getContentHash(),
                s.getStatus(), s.getNotes(), s.getCreatedAt(), s.getUpdatedAt()
        );
    }
}
