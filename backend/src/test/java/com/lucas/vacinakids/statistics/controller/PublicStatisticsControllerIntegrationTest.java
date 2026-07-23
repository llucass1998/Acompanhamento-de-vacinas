package com.lucas.vacinakids.statistics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.vacinakids.officialsource.entity.OfficialSource;
import com.lucas.vacinakids.officialsource.repository.OfficialSourceRepository;
import com.lucas.vacinakids.statistics.entity.PniStatisticsSnapshot;
import com.lucas.vacinakids.statistics.repository.PniStatisticsSnapshotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PublicStatisticsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PniStatisticsSnapshotRepository snapshotRepository;
    
    @Autowired
    private OfficialSourceRepository sourceRepository;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {

        jdbcTemplate.execute("DELETE FROM pni_statistics_snapshots");
        jdbcTemplate.execute("DELETE FROM import_items");
        jdbcTemplate.execute("DELETE FROM import_jobs");
        jdbcTemplate.execute("DELETE FROM vaccination_records");
        jdbcTemplate.execute("DELETE FROM vaccination_schedules");
        jdbcTemplate.execute("DELETE FROM children");
        jdbcTemplate.execute("DELETE FROM refresh_tokens");
        jdbcTemplate.execute("DELETE FROM admin_audit_logs");
        jdbcTemplate.execute("DELETE FROM campaigns");
        jdbcTemplate.execute("DELETE FROM calendar_rules");
        jdbcTemplate.execute("DELETE FROM calendar_versions");
        jdbcTemplate.execute("DELETE FROM official_sources");
        jdbcTemplate.execute("DELETE FROM vaccine_doses");
        jdbcTemplate.execute("DELETE FROM vaccines");
        jdbcTemplate.execute("DELETE FROM users");

        OfficialSource source = new OfficialSource();
        source.setSourceType("OPEN_DATA_API");
        source.setTitle("PNI 2026");
        source.setOrganization("MS");
        source.setReferenceYear(2026);
        source.setRetrievedAt(Instant.now());
        source.setContentHash("hash-stat");
        source.setStatus("ACTIVE");
        source = sourceRepository.save(source);

        PniStatisticsSnapshot snapshot = new PniStatisticsSnapshot();
        snapshot.setReferenceYear(2026);
        snapshot.setReferenceMonth(1);
        snapshot.setStateCode("SP");
        snapshot.setMunicipalityCode("3550308");
        snapshot.setVaccineCode("COVID_19");
        snapshot.setDoseCode("DOSE_1");
        snapshot.setAgeGroup("5-11");
        snapshot.setAppliedDoses(1500000L);
        snapshot.setSource(source);
        snapshot.setCollectedAt(Instant.now());
        snapshotRepository.save(snapshot);
    }

    @Test
    void listStatistics_shouldReturn200AndData() throws Exception {
        mockMvc.perform(get("/api/v1/public/statistics")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].vaccineCode").value("COVID_19"))
                .andExpect(jsonPath("$.content[0].appliedDoses").value(1500000));
    }
}
