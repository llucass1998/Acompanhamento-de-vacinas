package com.lucas.vacinakids.child.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.vacinakids.auth.dto.RegisterRequest;
import com.lucas.vacinakids.auth.security.JwtUtils;
import com.lucas.vacinakids.auth.security.UserDetailsImpl;
import com.lucas.vacinakids.child.dto.ChildCreateRequest;
import com.lucas.vacinakids.child.dto.ChildResponse;
import com.lucas.vacinakids.child.dto.ChildUpdateRequest;
import com.lucas.vacinakids.child.entity.Child;
import com.lucas.vacinakids.child.repository.ChildRepository;
import com.lucas.vacinakids.vaccinationrecord.repository.VaccinationRecordRepository;
import com.lucas.vacinakids.vaccination.repository.VaccinationScheduleRepository;
import com.lucas.vacinakids.user.entity.Role;
import com.lucas.vacinakids.user.entity.User;
import com.lucas.vacinakids.user.repository.UserRepository;
import com.lucas.vacinakids.auth.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.context.annotation.Import;
import com.lucas.vacinakids.TestcontainersConfiguration;

//@Import(TestcontainersConfiguration.class)
@SpringBootTest
@org.springframework.test.context.ActiveProfiles("test")
@AutoConfigureMockMvc
class ChildControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private com.lucas.vacinakids.auth.repository.RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private ChildRepository childRepository;
    @Autowired
    private com.lucas.vacinakids.vaccinationrecord.repository.VaccinationRecordRepository vaccinationRecordRepository;

    @Autowired
    private com.lucas.vacinakids.vaccination.repository.VaccinationScheduleRepository vaccinationScheduleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    private User user1;
    private User user2;
    private String token1;
    private String token2;

    @BeforeEach
    void setUp() {

        vaccinationRecordRepository.deleteAll();
        vaccinationScheduleRepository.deleteAll();
        childRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();

        user1 = userRepository.save(new User("User 1", "user1@email.com", passwordEncoder.encode("Password@123"), Role.USER));
        user2 = userRepository.save(new User("User 2", "user2@email.com", passwordEncoder.encode("Password@123"), Role.USER));

        token1 = "Bearer " + jwtUtils.generateAccessToken(UserDetailsImpl.build(user1));
        token2 = "Bearer " + jwtUtils.generateAccessToken(UserDetailsImpl.build(user2));
    }

    @Test
    void createChild_withValidData_shouldReturn201() throws Exception {
        ChildCreateRequest request = new ChildCreateRequest("Joãozinho", LocalDate.now().minusYears(1), "Maria", "Nenhuma observação");

        mockMvc.perform(post("/api/v1/children")
                .header("Authorization", token1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Joãozinho"));

        assertTrue(childRepository.findAll().stream().anyMatch(c -> c.getName().equals("Joãozinho")));
    }

    @Test
    void createChild_withFutureDate_shouldReturn400() throws Exception {
        ChildCreateRequest request = new ChildCreateRequest("Joãozinho", LocalDate.now().plusDays(1), "Maria", "");

        mockMvc.perform(post("/api/v1/children")
                .header("Authorization", token1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listChildren_shouldReturnOnlyUserChildren() throws Exception {
        childRepository.save(new Child(user1, "Child User 1", LocalDate.now(), "Resp 1", ""));
        childRepository.save(new Child(user2, "Child User 2", LocalDate.now(), "Resp 2", ""));

        mockMvc.perform(get("/api/v1/children")
                .header("Authorization", token1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Child User 1"));
    }

    @Test
    void getChildById_ownChild_shouldReturn200() throws Exception {
        Child child = childRepository.save(new Child(user1, "Child User 1", LocalDate.now(), "Resp 1", ""));

        mockMvc.perform(get("/api/v1/children/" + child.getId())
                .header("Authorization", token1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Child User 1"));
    }

    @Test
    void getChildById_otherUserChild_shouldReturn404() throws Exception {
        Child child = childRepository.save(new Child(user2, "Child User 2", LocalDate.now(), "Resp 2", ""));

        mockMvc.perform(get("/api/v1/children/" + child.getId())
                .header("Authorization", token1))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateChild_ownChild_shouldReturn200() throws Exception {
        Child child = childRepository.save(new Child(user1, "Child User 1", LocalDate.now(), "Resp 1", ""));

        ChildUpdateRequest request = new ChildUpdateRequest("Child User 1 Updated", LocalDate.now().minusDays(1), "Resp 1 Updated", "New notes");

        mockMvc.perform(put("/api/v1/children/" + child.getId())
                .header("Authorization", token1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Child User 1 Updated"));
    }

    @Test
    void updateChild_otherUserChild_shouldReturn404() throws Exception {
        Child child = childRepository.save(new Child(user2, "Child User 2", LocalDate.now(), "Resp 2", ""));

        ChildUpdateRequest request = new ChildUpdateRequest("Child User 2 Updated", LocalDate.now().minusDays(1), "Resp 2 Updated", "New notes");

        mockMvc.perform(put("/api/v1/children/" + child.getId())
                .header("Authorization", token1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteChild_shouldDeactivateInsteadOfHardDelete() throws Exception {
        Child child = childRepository.save(new Child(user1, "Child User 1", LocalDate.now(), "Resp 1", ""));

        mockMvc.perform(delete("/api/v1/children/" + child.getId())
                .header("Authorization", token1))
                .andExpect(status().isNoContent());

        Child deactivatedChild = childRepository.findById(child.getId()).orElseThrow();
        assertFalse(deactivatedChild.isActive());
        
        // Ensure it doesn't appear in lists anymore
        mockMvc.perform(get("/api/v1/children")
                .header("Authorization", token1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }
}
