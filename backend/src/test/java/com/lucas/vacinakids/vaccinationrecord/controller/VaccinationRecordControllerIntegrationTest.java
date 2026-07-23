package com.lucas.vacinakids.vaccinationrecord.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.vacinakids.auth.dto.LoginRequest;
import com.lucas.vacinakids.auth.dto.RegisterRequest;
import com.lucas.vacinakids.auth.repository.RefreshTokenRepository;
import com.lucas.vacinakids.child.entity.Child;
import com.lucas.vacinakids.child.repository.ChildRepository;
import com.lucas.vacinakids.user.entity.User;
import com.lucas.vacinakids.user.repository.UserRepository;
import com.lucas.vacinakids.vaccinationrecord.dto.VaccinationRecordRequest;
import com.lucas.vacinakids.vaccinationrecord.repository.VaccinationRecordRepository;
import com.lucas.vacinakids.vaccine.entity.Vaccine;
import com.lucas.vacinakids.vaccine.entity.VaccineDose;
import com.lucas.vacinakids.vaccine.repository.VaccineDoseRepository;
import com.lucas.vacinakids.vaccine.repository.VaccineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VaccinationRecordControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private VaccineRepository vaccineRepository;

    @Autowired
    private VaccineDoseRepository doseRepository;

    @Autowired
    private VaccinationRecordRepository recordRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private String authToken;
    private Child testChild;
    private VaccineDose testDose;

    @BeforeEach
    void setUp() throws Exception {


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

        String email = "record" + UUID.randomUUID().toString() + "@test.com";

        // Register and login to get token
        RegisterRequest registerReq = new RegisterRequest("Test User", email, "Password@123");
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isCreated());

        LoginRequest loginReq = new LoginRequest(email, "Password@123");
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andReturn();

        String responseStr = loginResult.getResponse().getContentAsString();
        authToken = "Bearer " + objectMapper.readTree(responseStr).get("accessToken").asText();

        // Setup User, Child and Vaccine Dose manually to bypass auth context limits
        User user = userRepository.findByEmail(email).orElseThrow();
        
        testChild = new Child(user, "Test Child", LocalDate.now().minusYears(1), "Parent", "");
        testChild = childRepository.save(testChild);

        Vaccine vaccine = Vaccine.builder()
                .name("Test Vaccine")
                .description("Desc")
                .code("TEST_VACCINE")
                .displayName("Test Vaccine Display")
                .official(false)
                .build();
        vaccine = vaccineRepository.save(vaccine);

        testDose = VaccineDose.builder()
                .vaccine(vaccine)
                
                .doseName("1ª Dose")
                .code("TEST_DOSE_1")
                .doseOrder(1)
                .description("Primeira Dose")
                .recommendedAgeMonths(2)
                .build();
        testDose = doseRepository.save(testDose);
    }

    @Test
    void registerDose_withValidData_shouldReturn201() throws Exception {
        VaccinationRecordRequest request = new VaccinationRecordRequest(
                testDose.getId(),
                LocalDate.now(),
                "Posto Central",
                "LOTE123",
                "Sem reações"
        );

        mockMvc.perform(post("/api/v1/children/{childId}/records", testChild.getId())
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.doseId").value(testDose.getId().toString()))
                .andExpect(jsonPath("$.vaccineName").value("Test Vaccine"));

        assertTrue(recordRepository.existsByChildIdAndDoseId(testChild.getId(), testDose.getId()));
    }

    @Test
    void registerDose_withFutureDate_shouldReturn400() throws Exception {
        VaccinationRecordRequest request = new VaccinationRecordRequest(
                testDose.getId(),
                LocalDate.now().plusDays(1),
                "Posto Central",
                "LOTE123",
                ""
        );

        mockMvc.perform(post("/api/v1/children/{childId}/records", testChild.getId())
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
