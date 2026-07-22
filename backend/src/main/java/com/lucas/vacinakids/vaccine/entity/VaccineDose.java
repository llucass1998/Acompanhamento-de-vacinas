package com.lucas.vacinakids.vaccine.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "vaccine_doses")
public class VaccineDose {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccine_id", nullable = false)
    private Vaccine vaccine;
    
    @Column(name = "dose_name", nullable = false)
    private String doseName;

    @Column(nullable = false)
    private String description;

    @Column(name = "recommended_age_months", nullable = false)
    private Integer recommendedAgeMonths;

    @Column(nullable = false)
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public VaccineDose() {}

    public VaccineDose(UUID id, Vaccine vaccine, String doseName, String description, Integer recommendedAgeMonths, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.vaccine = vaccine;
        this.doseName = doseName;
        this.description = description;
        this.recommendedAgeMonths = recommendedAgeMonths;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Vaccine getVaccine() { return vaccine; }
    public void setVaccine(Vaccine vaccine) { this.vaccine = vaccine; }
    
    public String getDoseName() { return doseName; }
    public void setDoseName(String doseName) { this.doseName = doseName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getRecommendedAgeMonths() { return recommendedAgeMonths; }
    public void setRecommendedAgeMonths(Integer recommendedAgeMonths) { this.recommendedAgeMonths = recommendedAgeMonths; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static class Builder {
        private UUID id;
        private Vaccine vaccine;
        private String doseName;
        private String description;
        private Integer recommendedAgeMonths;
        private boolean active = true;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder vaccine(Vaccine vaccine) { this.vaccine = vaccine; return this; }
        public Builder doseName(String doseName) { this.doseName = doseName; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder recommendedAgeMonths(Integer recommendedAgeMonths) { this.recommendedAgeMonths = recommendedAgeMonths; return this; }
        public Builder active(boolean active) { this.active = active; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public VaccineDose build() {
            return new VaccineDose(id, vaccine, doseName, description, recommendedAgeMonths, active, createdAt, updatedAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
