package com.lucas.vacinakids.vaccination.entity;

import com.lucas.vacinakids.child.entity.Child;
import com.lucas.vacinakids.vaccine.entity.VaccineDose;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "vaccination_schedules")
public class VaccinationSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dose_id", nullable = false)
    private VaccineDose dose;

    @Column(name = "expected_date", nullable = false)
    private LocalDate expectedDate;

    @Column(name = "applied_date")
    private LocalDate appliedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VaccinationStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public VaccinationSchedule() {}

    public VaccinationSchedule(UUID id, Child child, VaccineDose dose, LocalDate expectedDate, LocalDate appliedDate, VaccinationStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.child = child;
        this.dose = dose;
        this.expectedDate = expectedDate;
        this.appliedDate = appliedDate;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Child getChild() { return child; }
    public void setChild(Child child) { this.child = child; }

    public VaccineDose getDose() { return dose; }
    public void setDose(VaccineDose dose) { this.dose = dose; }

    public LocalDate getExpectedDate() { return expectedDate; }
    public void setExpectedDate(LocalDate expectedDate) { this.expectedDate = expectedDate; }

    public LocalDate getAppliedDate() { return appliedDate; }
    public void setAppliedDate(LocalDate appliedDate) { this.appliedDate = appliedDate; }

    public VaccinationStatus getStatus() { return status; }
    public void setStatus(VaccinationStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public enum VaccinationStatus {
        PENDING, APPLIED, LATE
    }

    public static class Builder {
        private UUID id;
        private Child child;
        private VaccineDose dose;
        private LocalDate expectedDate;
        private LocalDate appliedDate;
        private VaccinationStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder child(Child child) { this.child = child; return this; }
        public Builder dose(VaccineDose dose) { this.dose = dose; return this; }
        public Builder expectedDate(LocalDate expectedDate) { this.expectedDate = expectedDate; return this; }
        public Builder appliedDate(LocalDate appliedDate) { this.appliedDate = appliedDate; return this; }
        public Builder status(VaccinationStatus status) { this.status = status; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public VaccinationSchedule build() {
            return new VaccinationSchedule(id, child, dose, expectedDate, appliedDate, status, createdAt, updatedAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
