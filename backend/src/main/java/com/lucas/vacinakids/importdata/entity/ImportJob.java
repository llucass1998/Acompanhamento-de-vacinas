package com.lucas.vacinakids.importdata.entity;

import com.lucas.vacinakids.officialsource.entity.OfficialSource;
import com.lucas.vacinakids.user.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "import_jobs")
public class ImportJob {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private OfficialSource source;

    @Column(name = "import_type", nullable = false, length = 50)
    private String importType;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    @Column(name = "records_found", nullable = false)
    private Integer recordsFound = 0;

    @Column(name = "records_created", nullable = false)
    private Integer recordsCreated = 0;

    @Column(name = "records_updated", nullable = false)
    private Integer recordsUpdated = 0;

    @Column(name = "records_rejected", nullable = false)
    private Integer recordsRejected = 0;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executed_by")
    private User executedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public ImportJob() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public OfficialSource getSource() { return source; }
    public void setSource(OfficialSource source) { this.source = source; }
    public String getImportType() { return importType; }
    public void setImportType(String importType) { this.importType = importType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }
    public Instant getFinishedAt() { return finishedAt; }
    public void setFinishedAt(Instant finishedAt) { this.finishedAt = finishedAt; }
    public Integer getRecordsFound() { return recordsFound; }
    public void setRecordsFound(Integer recordsFound) { this.recordsFound = recordsFound; }
    public Integer getRecordsCreated() { return recordsCreated; }
    public void setRecordsCreated(Integer recordsCreated) { this.recordsCreated = recordsCreated; }
    public Integer getRecordsUpdated() { return recordsUpdated; }
    public void setRecordsUpdated(Integer recordsUpdated) { this.recordsUpdated = recordsUpdated; }
    public Integer getRecordsRejected() { return recordsRejected; }
    public void setRecordsRejected(Integer recordsRejected) { this.recordsRejected = recordsRejected; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public User getExecutedBy() { return executedBy; }
    public void setExecutedBy(User executedBy) { this.executedBy = executedBy; }
    public Instant getCreatedAt() { return createdAt; }
}
