package com.lucas.vacinakids.vaccinationrecord.controller;

import com.lucas.vacinakids.auth.security.UserDetailsImpl;
import com.lucas.vacinakids.vaccinationrecord.dto.VaccinationRecordRequest;
import com.lucas.vacinakids.vaccinationrecord.dto.VaccinationRecordResponse;
import com.lucas.vacinakids.vaccinationrecord.service.VaccinationRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Vaccination Record", description = "Registro e histórico de doses aplicadas")
@RequestMapping("/api/v1/children/{childId}/records")
public class VaccinationRecordController {

    private final VaccinationRecordService recordService;

    public VaccinationRecordController(VaccinationRecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping
    @Operation(summary = "Registrar a aplicação de uma dose de vacina em uma criança")
    public ResponseEntity<VaccinationRecordResponse> registerDose(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID childId,
            @Valid @RequestBody VaccinationRecordRequest request) {
        
        VaccinationRecordResponse response = recordService.registerDose(userDetails.getId(), childId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VaccinationRecordResponse>> getChildHistory(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID childId) {
            
        List<VaccinationRecordResponse> history = recordService.getChildHistory(userDetails.getId(), childId);
        return ResponseEntity.ok(history);
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<Void> deleteRecord(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID childId,
            @PathVariable UUID recordId) {
            
        recordService.deleteRecord(userDetails.getId(), childId, recordId);
        return ResponseEntity.noContent().build();
    }
}
