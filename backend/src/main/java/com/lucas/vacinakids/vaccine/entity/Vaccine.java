package com.lucas.vacinakids.vaccine.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "vaccines")
public class Vaccine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private boolean active = true;

    // Novos campos
    @Column(unique = true, length = 100)
    private String code;
    
    @Column(name = "display_name", length = 255)
    private String displayName;
    
    @Column(name = "prevented_diseases", columnDefinition = "TEXT")
    private String preventedDiseases;
    
    @Column(nullable = false)
    private boolean official = false;
    
    @Column(name = "source_id")
    private UUID sourceId;

    @Version
    @Column(name = "version_number", nullable = false)
    private Long versionNumber = 0L;

    @OneToMany(mappedBy = "vaccine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VaccineDose> doses = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Vaccine() {}

    public Vaccine(UUID id, String name, String description, boolean active, List<VaccineDose> doses, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.doses = doses != null ? doses : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getPreventedDiseases() { return preventedDiseases; }
    public void setPreventedDiseases(String preventedDiseases) { this.preventedDiseases = preventedDiseases; }

    public boolean isOfficial() { return official; }
    public void setOfficial(boolean official) { this.official = official; }

    public UUID getSourceId() { return sourceId; }
    public void setSourceId(UUID sourceId) { this.sourceId = sourceId; }

    public Long getVersionNumber() { return versionNumber; }
    public void setVersionNumber(Long versionNumber) { this.versionNumber = versionNumber; }

    public List<VaccineDose> getDoses() { return doses; }
    public void setDoses(List<VaccineDose> doses) { this.doses = doses; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public void addDose(VaccineDose dose) {
        doses.add(dose);
        dose.setVaccine(this);
    }


    public static class Builder {
        private UUID id;
        private String name;
        private String description;
        private boolean active = true;
        private List<VaccineDose> doses = new ArrayList<>();
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder active(boolean active) { this.active = active; return this; }
        public Builder doses(List<VaccineDose> doses) { this.doses = doses; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Vaccine build() {
            return new Vaccine(id, name, description, active, doses, createdAt, updatedAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
