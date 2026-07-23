package com.lucas.vacinakids.calendar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.vacinakids.auth.security.UserDetailsImpl;
import com.lucas.vacinakids.calendar.dto.CalendarRuleRequest;
import com.lucas.vacinakids.calendar.entity.CalendarVersion;
import com.lucas.vacinakids.calendar.repository.CalendarVersionRepository;
import com.lucas.vacinakids.child.repository.ChildRepository;
import com.lucas.vacinakids.officialsource.entity.OfficialSource;
import com.lucas.vacinakids.officialsource.repository.OfficialSourceRepository;
import com.lucas.vacinakids.user.entity.Role;
import com.lucas.vacinakids.user.entity.User;
import com.lucas.vacinakids.user.repository.UserRepository;
import com.lucas.vacinakids.vaccine.entity.Vaccine;
import com.lucas.vacinakids.vaccine.entity.VaccineDose;
import com.lucas.vacinakids.vaccine.repository.VaccineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminCalendarRuleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private OfficialSourceRepository sourceRepository;

    @Autowired
    private CalendarVersionRepository versionRepository;
    
    @Autowired
    private VaccineRepository vaccineRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private User adminUser;
    private CalendarVersion version;
    private VaccineDose dose;

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

        adminUser = new User();
        adminUser.setEmail("admin.test@vacinakids.com");
        adminUser.setPasswordHash(passwordEncoder.encode("password"));
        adminUser.setName("Admin Test");
        adminUser.setRole(Role.ADMIN);
        adminUser = userRepository.save(adminUser);

        OfficialSource source = new OfficialSource();
        source.setSourceType("NATIONAL_CALENDAR");
        source.setTitle("PNI 2026");
        source.setOrganization("MS");
        source.setReferenceYear(2026);
        source.setRetrievedAt(Instant.now());
        source.setContentHash("hash");
        source.setStatus("ACTIVE");
        source = sourceRepository.save(source);

        version = new CalendarVersion();
        version.setName("Calendário 2026");
        version.setReferenceYear(2026);
        version.setVersion("v1.0");
        version.setSource(source);
        version.setStatus("DRAFT");
        version.setValidFrom(LocalDate.of(2026, 1, 1));
        version = versionRepository.save(version);
        
        Vaccine vaccine = Vaccine.builder()
            .name("BCG Test")
            .description("Desc")
            .active(true)
            .build();
        vaccine.setCode("BCG_TEST_1");
        vaccine.setDisplayName("BCG");
        vaccine.setOfficial(true);
        vaccine = vaccine = vaccineRepository.save(vaccine);
        
        dose = new VaccineDose();
        dose.setDoseName("Dose Única");
        dose.setDoseOrder(1);
        dose.setVaccine(vaccine);
        dose.setCode("BCG_TEST_1_DOSE_1");
        
        vaccine.addDose(dose);
        vaccine = vaccineRepository.save(vaccine);
        
        dose = vaccine.getDoses().get(0);
    }

    @Test
    void createCalendarRule_asAdmin_shouldReturn201() throws Exception {
        CalendarRuleRequest request = new CalendarRuleRequest(
                version.getId(), dose.getId(), "CHILD", "Recém Nascido", 0, null, "Ao nascer", true
        );

        mockMvc.perform(post("/api/v1/admin/calendar-rules")
                .with(user(new UserDetailsImpl(
                        adminUser.getId(), 
                        adminUser.getName(),
                        adminUser.getEmail(), 
                        adminUser.getPasswordHash(),
                        true,
                        false,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + adminUser.getRole().name()))
                )))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lifeStage").value("CHILD"));
    }
}
