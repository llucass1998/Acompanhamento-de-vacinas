package com.lucas.vacinakids.calendar.repository;

import com.lucas.vacinakids.calendar.entity.CalendarRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CalendarRuleRepository extends JpaRepository<CalendarRule, UUID> {
    List<CalendarRule> findAllByCalendarVersionIdAndActiveTrue(UUID versionId);
}
