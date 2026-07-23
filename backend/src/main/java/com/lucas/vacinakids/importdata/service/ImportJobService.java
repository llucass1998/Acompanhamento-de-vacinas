package com.lucas.vacinakids.importdata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.vacinakids.importdata.dto.ImportJobRequest;
import com.lucas.vacinakids.importdata.dto.ImportJobResponse;
import com.lucas.vacinakids.importdata.entity.ImportJob;
import com.lucas.vacinakids.importdata.repository.ImportJobRepository;
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
public class ImportJobService {

    private final ImportJobRepository repository;
    private final OfficialSourceRepository sourceRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    public ImportJobService(ImportJobRepository repository, OfficialSourceRepository sourceRepository,
                            UserRepository userRepository, AuditService auditService, ObjectMapper objectMapper) {
        this.repository = repository;
        this.sourceRepository = sourceRepository;
        this.userRepository = userRepository;
        this.auditService = auditService;
        this.objectMapper = objectMapper;
    }

    public Page<ImportJobResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    public ImportJobResponse findById(UUID id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Import job not found"));
    }

    @Transactional
    public ImportJobResponse createJob(UUID adminId, ImportJobRequest request) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Admin not found"));

        OfficialSource source = null;
        if (request.sourceId() != null) {
            source = sourceRepository.findById(request.sourceId())
                    .orElseThrow(() -> new NotFoundException("Source not found"));
        }

        ImportJob job = new ImportJob();
        job.setSource(source);
        job.setImportType(request.importType());
        job.setStatus("PENDING");
        job.setExecutedBy(admin);

        job = repository.save(job);

        try {
            String afterData = objectMapper.writeValueAsString(request);
            auditService.logAdminAction(adminId, "CREATE_IMPORT_JOB", "ImportJob", job.getId(), null, afterData, "Scheduled import job");
        } catch (JsonProcessingException ignored) {}

        return toResponse(job);
    }

    private ImportJobResponse toResponse(ImportJob job) {
        return new ImportJobResponse(
                job.getId(),
                job.getSource() != null ? job.getSource().getId() : null,
                job.getImportType(),
                job.getStatus(),
                job.getStartedAt(),
                job.getFinishedAt(),
                job.getRecordsFound(),
                job.getRecordsCreated(),
                job.getRecordsUpdated(),
                job.getRecordsRejected(),
                job.getErrorMessage(),
                job.getExecutedBy() != null ? job.getExecutedBy().getId() : null,
                job.getCreatedAt()
        );
    }
}
