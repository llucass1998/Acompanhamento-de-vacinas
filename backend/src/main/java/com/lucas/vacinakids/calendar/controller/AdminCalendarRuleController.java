package com.lucas.vacinakids.calendar.controller;

import com.lucas.vacinakids.auth.security.UserDetailsImpl;
import com.lucas.vacinakids.calendar.dto.CalendarRuleRequest;
import com.lucas.vacinakids.calendar.dto.CalendarRuleResponse;
import com.lucas.vacinakids.calendar.service.CalendarRuleService;
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
@RequestMapping("/api/v1/admin/calendar-rules")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCalendarRuleController {

    private final CalendarRuleService service;

    public AdminCalendarRuleController(CalendarRuleService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<CalendarRuleResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalendarRuleResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<CalendarRuleResponse> create(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CalendarRuleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(userDetails.getId(), request));
    }
}
