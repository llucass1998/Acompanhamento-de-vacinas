package com.lucas.vacinakids.statistics.controller;

import com.lucas.vacinakids.statistics.dto.PniStatisticsSnapshotResponse;
import com.lucas.vacinakids.statistics.service.PniStatisticsSnapshotService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public/statistics")
public class PublicStatisticsController {

    private final PniStatisticsSnapshotService service;

    public PublicStatisticsController(PniStatisticsSnapshotService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<PniStatisticsSnapshotResponse>> list(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<Page<PniStatisticsSnapshotResponse>> listByYear(@PathVariable Integer year, Pageable pageable) {
        return ResponseEntity.ok(service.findByYear(year, pageable));
    }
}
