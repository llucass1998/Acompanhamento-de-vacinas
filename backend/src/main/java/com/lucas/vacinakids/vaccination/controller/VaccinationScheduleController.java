package com.lucas.vacinakids.vaccination.controller;

import com.lucas.vacinakids.auth.security.UserDetailsImpl;
import com.lucas.vacinakids.vaccination.dto.VaccinationScheduleResponse;
import com.lucas.vacinakids.vaccination.dto.VaccinationSummaryResponse;
import com.lucas.vacinakids.vaccination.service.VaccinationScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Vaccination Schedule", description = "Consulta do calendário vacinal previsto")
@RequestMapping("/api/v1/children/{childId}")
public class VaccinationScheduleController {

    private final VaccinationScheduleService scheduleService;

    public VaccinationScheduleController(VaccinationScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/vaccination-schedule")
    @Operation(summary = "Obter o calendário de vacinas previstas de uma criança")
    public ResponseEntity<List<VaccinationScheduleResponse>> getChildSchedule(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID childId) {
        return ResponseEntity.ok(scheduleService.getChildSchedule(userDetails.getId(), childId));
    }

    @GetMapping("/vaccination-summary")
    public ResponseEntity<VaccinationSummaryResponse> getChildSummary(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID childId) {
        return ResponseEntity.ok(scheduleService.getChildSummary(userDetails.getId(), childId));
    }
}
