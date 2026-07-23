package com.lucas.vacinakids.officialsource.controller;

import com.lucas.vacinakids.auth.security.UserDetailsImpl;
import com.lucas.vacinakids.officialsource.dto.OfficialSourceRequest;
import com.lucas.vacinakids.officialsource.dto.OfficialSourceResponse;
import com.lucas.vacinakids.officialsource.service.OfficialSourceService;
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
@RequestMapping("/api/v1/admin/official-sources")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOfficialSourceController {

    private final OfficialSourceService service;

    public AdminOfficialSourceController(OfficialSourceService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<OfficialSourceResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfficialSourceResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<OfficialSourceResponse> create(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody OfficialSourceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(userDetails.getId(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfficialSourceResponse> update(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID id,
            @Valid @RequestBody OfficialSourceRequest request) {
        return ResponseEntity.ok(service.update(userDetails.getId(), id, request));
    }
}
