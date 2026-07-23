package com.lucas.vacinakids.campaign.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.vacinakids.auth.dto.LoginRequest;
import com.lucas.vacinakids.auth.dto.RegisterRequest;
import com.lucas.vacinakids.campaign.dto.CampaignRequest;
import com.lucas.vacinakids.campaign.entity.Campaign;
import com.lucas.vacinakids.campaign.repository.CampaignRepository;
import com.lucas.vacinakids.child.repository.ChildRepository;
import com.lucas.vacinakids.user.entity.Role;
import com.lucas.vacinakids.user.entity.User;
import com.lucas.vacinakids.user.repository.UserRepository;
import com.lucas.vacinakids.auth.repository.RefreshTokenRepository;
import com.lucas.vacinakids.vaccination.repository.VaccinationScheduleRepository;
import com.lucas.vacinakids.vaccinationrecord.repository.VaccinationRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CampaignControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CampaignRepository campaignRepository;
    
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private VaccinationScheduleRepository vaccinationScheduleRepository;

    @Autowired
    private VaccinationRecordRepository vaccinationRecordRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String userToken;
    private String adminToken;
    private Campaign testCampaign;

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

        // Setup USER
        User user = new User();
        user.setName("Test User");
        user.setEmail("user@example.com");
        user.setPasswordHash(passwordEncoder.encode("Password@123"));
        user.setRole(Role.USER);
        userRepository.save(user);

        // Setup ADMIN
        User admin = new User();
        admin.setName("Admin User");
        admin.setEmail("admin@example.com");
        admin.setPasswordHash(passwordEncoder.encode("Password@123"));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        // Get USER token
        LoginRequest userLogin = new LoginRequest("user@example.com", "Password@123");
        MvcResult userResult = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userLogin)))
                .andReturn();
        userToken = objectMapper.readTree(userResult.getResponse().getContentAsString()).get("accessToken").asText();

        // Get ADMIN token
        LoginRequest adminLogin = new LoginRequest("admin@example.com", "Password@123");
        MvcResult adminResult = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminLogin)))
                .andReturn();
        adminToken = objectMapper.readTree(adminResult.getResponse().getContentAsString()).get("accessToken").asText();

        // Setup test campaign
        testCampaign = Campaign.builder()
                .title("Campanha de Polio")
                .description("Vacinacao contra poliomielite")
                .targetAudience("Criancas menores de 5 anos")
                .startDate(LocalDate.now().minusDays(2))
                .endDate(LocalDate.now().plusDays(10))
                .active(true)
                .build();
        testCampaign = campaignRepository.save(testCampaign);
    }

    @Test
    void getActiveCampaigns_asUser_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/campaigns/active")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].title").value("Campanha de Polio"));
    }

    @Test
    void createCampaign_asUser_shouldReturn403() throws Exception {
        CampaignRequest request = new CampaignRequest(
                "Nova Campanha",
                "Descricao",
                "Publico",
                LocalDate.now(),
                LocalDate.now().plusDays(5)
        );

        mockMvc.perform(post("/api/v1/admin/campaigns")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createCampaign_asAdmin_shouldReturn201() throws Exception {
        CampaignRequest request = new CampaignRequest(
                "Nova Campanha",
                "Descricao",
                "Publico",
                LocalDate.now(),
                LocalDate.now().plusDays(5)
        );

        mockMvc.perform(post("/api/v1/admin/campaigns")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Nova Campanha"));
    }

    @Test
    void createCampaign_withInvalidDates_shouldReturn422() throws Exception {
        CampaignRequest request = new CampaignRequest(
                "Nova Campanha",
                "Descricao",
                "Publico",
                LocalDate.now().plusDays(5),
                LocalDate.now() // End date before start date
        );

        mockMvc.perform(post("/api/v1/admin/campaigns")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }
}
