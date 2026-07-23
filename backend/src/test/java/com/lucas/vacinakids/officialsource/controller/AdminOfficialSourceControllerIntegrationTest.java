package com.lucas.vacinakids.officialsource.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.vacinakids.auth.security.UserDetailsImpl;
import com.lucas.vacinakids.child.repository.ChildRepository;
import com.lucas.vacinakids.officialsource.dto.OfficialSourceRequest;
import com.lucas.vacinakids.user.entity.Role;
import com.lucas.vacinakids.user.entity.User;
import com.lucas.vacinakids.user.repository.UserRepository;
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

import java.time.Instant;
import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminOfficialSourceControllerIntegrationTest {

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
    private PasswordEncoder passwordEncoder;

    private User adminUser;
    private User normalUser;

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

        adminUser = new User();
        adminUser.setEmail("admin.test@vacinakids.com");
        adminUser.setPasswordHash(passwordEncoder.encode("password"));
        adminUser.setName("Admin Test");
        adminUser.setRole(Role.ADMIN);
        adminUser = userRepository.save(adminUser);

        normalUser = new User();
        normalUser.setEmail("user.test@vacinakids.com");
        normalUser.setPasswordHash(passwordEncoder.encode("password"));
        normalUser.setName("Normal User Test");
        normalUser.setRole(Role.USER);
        normalUser = userRepository.save(normalUser);
    }

    @Test
    void createSource_asAdmin_shouldReturn201() throws Exception {
        OfficialSourceRequest request = new OfficialSourceRequest(
                "NATIONAL_CALENDAR", "Catálogo Nacional de Vacinação 2026", "Ministério da Saúde",
                "https://gov.br/saude", 2026, Instant.parse("2026-01-01T00:00:00Z"),
                "hash123", "ACTIVE", "Notas de teste"
        );

        mockMvc.perform(post("/api/v1/admin/official-sources")
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
                .andExpect(jsonPath("$.title").value("Catálogo Nacional de Vacinação 2026"));
    }

    @Test
    void createSource_asUser_shouldReturn403() throws Exception {
        OfficialSourceRequest request = new OfficialSourceRequest(
                "NATIONAL_CALENDAR", "Catálogo", "Org", "url", 2026, Instant.now(), "hash", "ACTIVE", null
        );

        mockMvc.perform(post("/api/v1/admin/official-sources")
                .with(user(new UserDetailsImpl(
                        normalUser.getId(), 
                        normalUser.getName(),
                        normalUser.getEmail(), 
                        normalUser.getPasswordHash(), 
                        true,
                        false,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + normalUser.getRole().name()))
                )))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
