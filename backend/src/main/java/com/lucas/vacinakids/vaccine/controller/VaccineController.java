package com.lucas.vacinakids.vaccine.controller;

import com.lucas.vacinakids.vaccine.dto.VaccineResponse;
import com.lucas.vacinakids.vaccine.service.VaccineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vaccines")
public class VaccineController {

    private final VaccineService vaccineService;

    public VaccineController(VaccineService vaccineService) {
        this.vaccineService = vaccineService;
    }

    @GetMapping
    public ResponseEntity<List<VaccineResponse>> listVaccines() {
        return ResponseEntity.ok(vaccineService.getAllVaccines());
    }

    @GetMapping("/{vaccineId}")
    public ResponseEntity<VaccineResponse> getVaccineById(@PathVariable UUID vaccineId) {
        return ResponseEntity.ok(vaccineService.getVaccineById(vaccineId));
    }
}
