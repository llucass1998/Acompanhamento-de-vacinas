package com.lucas.vacinakids.vaccinationrecord.entity;

import com.lucas.vacinakids.child.entity.Child;
import com.lucas.vacinakids.vaccine.entity.VaccineDose;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vaccination_records", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"child_id", "dose_id"})
})
public class VaccinationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dose_id", nullable = false)
    private VaccineDose dose;

    @Column(name = "applied_date", nullable = false)
    private LocalDate appliedDate;

    @Column(length = 255)
    private String location;

    @Column(name = "batch_number", length = 100)
    private String batchNumber;

    @Column(length = 500)
    private String observations;

    @Column(name = "proof_url", length = 500)
    private String proofUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public VaccinationRecord() {}

    public VaccinationRecord(UUID id, Child child, VaccineDose dose, LocalDate appliedDate, 
                           String location, String batchNumber, String observations, String proofUrl,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.child = child;
        this.dose = dose;
        this.appliedDate = appliedDate;
        this.location = location;
        this.batchNumber = batchNumber;
        this.observations = observations;
        this.proofUrl = proofUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Child getChild() { return child; }
    public void setChild(Child child) { this.child = child; }

    public VaccineDose getDose() { return dose; }
    public void setDose(VaccineDose dose) { this.dose = dose; }

    public LocalDate getAppliedDate() { return appliedDate; }
    public void setAppliedDate(LocalDate appliedDate) { this.appliedDate = appliedDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }

    public String getProofUrl() { return proofUrl; }
    public void setProofUrl(String proofUrl) { this.proofUrl = proofUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static class Builder {
        private UUID id;
        private Child child;
        private VaccineDose dose;
        private LocalDate appliedDate;
        private String location;
        private String batchNumber;
        private String observations;
        private String proofUrl;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder child(Child child) { this.child = child; return this; }
        public Builder dose(VaccineDose dose) { this.dose = dose; return this; }
        public Builder appliedDate(LocalDate appliedDate) { this.appliedDate = appliedDate; return this; }
        public Builder location(String location) { this.location = location; return this; }
        public Builder batchNumber(String batchNumber) { this.batchNumber = batchNumber; return this; }
        public Builder observations(String observations) { this.observations = observations; return this; }
        public Builder proofUrl(String proofUrl) { this.proofUrl = proofUrl; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public VaccinationRecord build() {
            return new VaccinationRecord(id, child, dose, appliedDate, location, batchNumber, observations, proofUrl, createdAt, updatedAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
