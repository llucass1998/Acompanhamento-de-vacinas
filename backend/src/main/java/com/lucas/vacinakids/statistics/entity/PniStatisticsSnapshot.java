package com.lucas.vacinakids.statistics.entity;

import com.lucas.vacinakids.officialsource.entity.OfficialSource;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "pni_statistics_snapshots")
public class PniStatisticsSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "reference_year", nullable = false)
    private Integer referenceYear;

    @Column(name = "reference_month")
    private Integer referenceMonth;

    @Column(name = "state_code", length = 10)
    private String stateCode;

    @Column(name = "municipality_code", length = 20)
    private String municipalityCode;

    @Column(name = "vaccine_code", length = 100)
    private String vaccineCode;

    @Column(name = "dose_code", length = 100)
    private String doseCode;

    @Column(name = "age_group", length = 100)
    private String ageGroup;

    @Column(name = "applied_doses", nullable = false)
    private Long appliedDoses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id", nullable = false)
    private OfficialSource source;

    @Column(name = "collected_at", nullable = false)
    private Instant collectedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public PniStatisticsSnapshot() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Integer getReferenceYear() { return referenceYear; }
    public void setReferenceYear(Integer referenceYear) { this.referenceYear = referenceYear; }
    public Integer getReferenceMonth() { return referenceMonth; }
    public void setReferenceMonth(Integer referenceMonth) { this.referenceMonth = referenceMonth; }
    public String getStateCode() { return stateCode; }
    public void setStateCode(String stateCode) { this.stateCode = stateCode; }
    public String getMunicipalityCode() { return municipalityCode; }
    public void setMunicipalityCode(String municipalityCode) { this.municipalityCode = municipalityCode; }
    public String getVaccineCode() { return vaccineCode; }
    public void setVaccineCode(String vaccineCode) { this.vaccineCode = vaccineCode; }
    public String getDoseCode() { return doseCode; }
    public void setDoseCode(String doseCode) { this.doseCode = doseCode; }
    public String getAgeGroup() { return ageGroup; }
    public void setAgeGroup(String ageGroup) { this.ageGroup = ageGroup; }
    public Long getAppliedDoses() { return appliedDoses; }
    public void setAppliedDoses(Long appliedDoses) { this.appliedDoses = appliedDoses; }
    public OfficialSource getSource() { return source; }
    public void setSource(OfficialSource source) { this.source = source; }
    public Instant getCollectedAt() { return collectedAt; }
    public void setCollectedAt(Instant collectedAt) { this.collectedAt = collectedAt; }
    public Instant getCreatedAt() { return createdAt; }
}
