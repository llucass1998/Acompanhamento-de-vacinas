package com.lucas.vacinakids.importdata.controller;

import com.lucas.vacinakids.auth.security.UserDetailsImpl;
import com.lucas.vacinakids.importdata.dto.ImportJobRequest;
import com.lucas.vacinakids.importdata.dto.ImportJobResponse;
import com.lucas.vacinakids.importdata.service.ImportJobService;
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
@RequestMapping("/api/v1/admin/import-jobs")
@PreAuthorize("hasRole('ADMIN')")
public class AdminImportJobController {

    private final ImportJobService service;

    public AdminImportJobController(ImportJobService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ImportJobResponse>> list(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImportJobResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ImportJobResponse> create(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                    @Valid @RequestBody ImportJobRequest request) {
        ImportJobResponse response = service.createJob(userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
