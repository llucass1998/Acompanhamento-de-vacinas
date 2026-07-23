package com.lucas.vacinakids.importdata.entity;

import com.lucas.vacinakids.user.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "import_items")
public class ImportItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "import_job_id", nullable = false)
    private ImportJob importJob;

    @Column(name = "entity_type", nullable = false, length = 100)
    private String entityType;

    @Column(name = "external_identifier", length = 255)
    private String externalIdentifier;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "original_payload", nullable = false, columnDefinition = "jsonb")
    private String originalPayload;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "normalized_payload", columnDefinition = "jsonb")
    private String normalizedPayload;

    @Column(name = "validation_status", nullable = false, length = 50)
    private String validationStatus;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "validation_messages", columnDefinition = "jsonb")
    private String validationMessages;

    @Column(length = 50)
    private String action;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    @Column(name = "reviewed_at")
    private Instant reviewedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public ImportItem() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public ImportJob getImportJob() { return importJob; }
    public void setImportJob(ImportJob importJob) { this.importJob = importJob; }
    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }
    public String getExternalIdentifier() { return externalIdentifier; }
    public void setExternalIdentifier(String externalIdentifier) { this.externalIdentifier = externalIdentifier; }
    public String getOriginalPayload() { return originalPayload; }
    public void setOriginalPayload(String originalPayload) { this.originalPayload = originalPayload; }
    public String getNormalizedPayload() { return normalizedPayload; }
    public void setNormalizedPayload(String normalizedPayload) { this.normalizedPayload = normalizedPayload; }
    public String getValidationStatus() { return validationStatus; }
    public void setValidationStatus(String validationStatus) { this.validationStatus = validationStatus; }
    public String getValidationMessages() { return validationMessages; }
    public void setValidationMessages(String validationMessages) { this.validationMessages = validationMessages; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public User getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(User reviewedBy) { this.reviewedBy = reviewedBy; }
    public Instant getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(Instant reviewedAt) { this.reviewedAt = reviewedAt; }
    public Instant getCreatedAt() { return createdAt; }
}
