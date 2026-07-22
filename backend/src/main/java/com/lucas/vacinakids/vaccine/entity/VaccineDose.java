package com.lucas.vacinakids.vaccine.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "vaccine_doses", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"vaccine_id", "code"})
})
public class VaccineDose {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccine_id", nullable = false)
    private Vaccine vaccine;

    @Column(name = "dose_name", nullable = false)
    private String doseName;

    @Column(name = "recommended_age_months", nullable = false)
    private int recommendedAgeMonths;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private boolean active = true;

    // Novos campos
    @Column(length = 100, nullable = false)
    private String code;
    
    @Column(name = "dose_order")
    private Integer doseOrder;
    
    @Column(name = "source_id")
    private UUID sourceId;

    @Version
    @Column(name = "version_number", nullable = false)
    private Long versionNumber = 0L;

    @Column(name = "source", length = 100)
    private String source;

    @Column(name = "source_version", length = 50)
    private String sourceVersion;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public VaccineDose() {}

    public VaccineDose(UUID id, Vaccine vaccine, String doseName, int recommendedAgeMonths, String description, boolean active, String source, String sourceVersion, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.vaccine = vaccine;
        this.doseName = doseName;
        this.recommendedAgeMonths = recommendedAgeMonths;
        this.description = description;
        this.active = active;
        this.source = source;
        this.sourceVersion = sourceVersion;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Vaccine getVaccine() { return vaccine; }
    public void setVaccine(Vaccine vaccine) { this.vaccine = vaccine; }

    public String getDoseName() { return doseName; }
    public void setDoseName(String doseName) { this.doseName = doseName; }

    public int getRecommendedAgeMonths() { return recommendedAgeMonths; }
    public void setRecommendedAgeMonths(int recommendedAgeMonths) { this.recommendedAgeMonths = recommendedAgeMonths; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Integer getDoseOrder() { return doseOrder; }
    public void setDoseOrder(Integer doseOrder) { this.doseOrder = doseOrder; }

    public UUID getSourceId() { return sourceId; }
    public void setSourceId(UUID sourceId) { this.sourceId = sourceId; }

    public Long getVersionNumber() { return versionNumber; }
    public void setVersionNumber(Long versionNumber) { this.versionNumber = versionNumber; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getSourceVersion() { return sourceVersion; }
    public void setSourceVersion(String sourceVersion) { this.sourceVersion = sourceVersion; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }


    public static class Builder {
        private UUID id;
        private Vaccine vaccine;
        private String doseName;
        private int recommendedAgeMonths;
        private String description;
        private boolean active = true;
        private String source;
        private String sourceVersion;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder vaccine(Vaccine vaccine) { this.vaccine = vaccine; return this; }
        public Builder doseName(String doseName) { this.doseName = doseName; return this; }
        public Builder recommendedAgeMonths(int recommendedAgeMonths) { this.recommendedAgeMonths = recommendedAgeMonths; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder active(boolean active) { this.active = active; return this; }
        public Builder source(String source) { this.source = source; return this; }
        public Builder sourceVersion(String sourceVersion) { this.sourceVersion = sourceVersion; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public VaccineDose build() {
            return new VaccineDose(id, vaccine, doseName, recommendedAgeMonths, description, active, source, sourceVersion, createdAt, updatedAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
