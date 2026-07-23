package com.lucas.vacinakids.calendar.entity;

import com.lucas.vacinakids.officialsource.entity.OfficialSource;
import com.lucas.vacinakids.vaccine.entity.VaccineDose;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "calendar_rules")
public class CalendarRule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_version_id", nullable = false)
    private CalendarVersion calendarVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccine_dose_id", nullable = false)
    private VaccineDose vaccineDose;

    @Column(name = "life_stage", nullable = false, length = 50)
    private String lifeStage;

    @Column(nullable = false, length = 255)
    private String audience;

    @Column(name = "recommended_age_days")
    private Integer recommendedAgeDays;

    @Column(name = "recommended_age_months")
    private Integer recommendedAgeMonths;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id", nullable = false)
    private OfficialSource source;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    @Column(name = "version_number", nullable = false)
    private Long versionNumber = 0L;

    public CalendarRule() {}

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public CalendarVersion getCalendarVersion() { return calendarVersion; }
    public void setCalendarVersion(CalendarVersion calendarVersion) { this.calendarVersion = calendarVersion; }
    public VaccineDose getVaccineDose() { return vaccineDose; }
    public void setVaccineDose(VaccineDose vaccineDose) { this.vaccineDose = vaccineDose; }
    public String getLifeStage() { return lifeStage; }
    public void setLifeStage(String lifeStage) { this.lifeStage = lifeStage; }
    public String getAudience() { return audience; }
    public void setAudience(String audience) { this.audience = audience; }
    public Integer getRecommendedAgeDays() { return recommendedAgeDays; }
    public void setRecommendedAgeDays(Integer recommendedAgeDays) { this.recommendedAgeDays = recommendedAgeDays; }
    public Integer getRecommendedAgeMonths() { return recommendedAgeMonths; }
    public void setRecommendedAgeMonths(Integer recommendedAgeMonths) { this.recommendedAgeMonths = recommendedAgeMonths; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public OfficialSource getSource() { return source; }
    public void setSource(OfficialSource source) { this.source = source; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Long getVersionNumber() { return versionNumber; }
    public void setVersionNumber(Long versionNumber) { this.versionNumber = versionNumber; }
}
