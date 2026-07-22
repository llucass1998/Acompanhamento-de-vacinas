package com.lucas.vacinakids.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.vacinakids.TestcontainersConfiguration;
import com.lucas.vacinakids.auth.dto.LoginRequest;
import com.lucas.vacinakids.auth.dto.RegisterRequest;
import com.lucas.vacinakids.auth.dto.TokenRefreshRequest;
import com.lucas.vacinakids.user.entity.User;
import com.lucas.vacinakids.user.repository.UserRepository;
import com.lucas.vacinakids.auth.repository.RefreshTokenRepository;
import com.lucas.vacinakids.child.repository.ChildRepository;
import com.lucas.vacinakids.vaccination.repository.VaccinationScheduleRepository;
import com.lucas.vacinakids.vaccinationrecord.repository.VaccinationRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private ChildRepository childRepository;
    @Autowired
    private VaccinationScheduleRepository vaccinationScheduleRepository;
    @Autowired
    private VaccinationRecordRepository vaccinationRecordRepository;

    @BeforeEach
    void setUp() {
        vaccinationRecordRepository.deleteAll();
        vaccinationScheduleRepository.deleteAll();
        childRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void register_withValidData_shouldReturn201() throws Exception {
        RegisterRequest request = new RegisterRequest("Test User", "test@email.com", "Password@123");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        assertTrue(userRepository.existsByEmail("test@email.com"));
    }

    @Test
    void register_withDuplicateEmail_shouldReturn409() throws Exception {
        RegisterRequest request = new RegisterRequest("Test User", "test@email.com", "Password@123");

        // First registration
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Second registration
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void login_withValidCredentials_shouldReturnTokens() throws Exception {
        // Register first
        RegisterRequest registerReq = new RegisterRequest("Test User", "test@email.com", "Password@123");
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerReq)));

        // Then login
        LoginRequest loginReq = new LoginRequest("test@email.com", "Password@123");
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.user.email").value("test@email.com"));
    }

    @Test
    void login_withInvalidPassword_shouldReturn401() throws Exception {
        // Register first
        RegisterRequest registerReq = new RegisterRequest("Test User", "test@email.com", "Password@123");
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerReq)));

        // Then login with wrong password
        LoginRequest loginReq = new LoginRequest("test@email.com", "WrongPassword@123");
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedRoute_withoutToken_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void refreshToken_withValidToken_shouldReturnNewTokens() throws Exception {
        // Register and Login
        RegisterRequest registerReq = new RegisterRequest("Test User", "refresh@email.com", "Password@123");
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerReq)));

        LoginRequest loginReq = new LoginRequest("refresh@email.com", "Password@123");
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginReq)))
                .andReturn();

        String responseStr = result.getResponse().getContentAsString();
        String refreshToken = objectMapper.readTree(responseStr).get("refreshToken").asText();

        // Refresh
        TokenRefreshRequest refreshReq = new TokenRefreshRequest(refreshToken);
        mockMvc.perform(post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }
}
