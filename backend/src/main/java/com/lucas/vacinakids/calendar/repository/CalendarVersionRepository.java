package com.lucas.vacinakids.calendar.repository;

import com.lucas.vacinakids.calendar.entity.CalendarVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CalendarVersionRepository extends JpaRepository<CalendarVersion, UUID> {
    Optional<CalendarVersion> findFirstByStatusOrderByValidFromDesc(String status);
}
