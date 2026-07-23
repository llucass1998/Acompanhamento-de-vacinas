package com.lucas.vacinakids.calendar.controller;

import com.lucas.vacinakids.auth.security.UserDetailsImpl;
import com.lucas.vacinakids.calendar.dto.CalendarVersionRequest;
import com.lucas.vacinakids.calendar.dto.CalendarVersionResponse;
import com.lucas.vacinakids.calendar.service.CalendarVersionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/calendar-versions")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCalendarVersionController {

    private final CalendarVersionService service;

    public AdminCalendarVersionController(CalendarVersionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<CalendarVersionResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalendarVersionResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<CalendarVersionResponse> create(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CalendarVersionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(userDetails.getId(), request));
    }
}
