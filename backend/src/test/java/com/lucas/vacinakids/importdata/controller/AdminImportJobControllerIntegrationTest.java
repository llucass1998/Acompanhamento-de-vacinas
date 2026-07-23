package com.lucas.vacinakids.importdata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.vacinakids.auth.security.UserDetailsImpl;
import com.lucas.vacinakids.importdata.dto.ImportJobRequest;
import com.lucas.vacinakids.importdata.entity.ImportJob;
import com.lucas.vacinakids.importdata.repository.ImportJobRepository;
import com.lucas.vacinakids.user.entity.Role;
import com.lucas.vacinakids.user.entity.User;
import com.lucas.vacinakids.user.repository.UserRepository;
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

import java.util.Collections;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminImportJobControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ImportJobRepository importJobRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private User adminUser;

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
        adminUser.setEmail("admin.import@vacinakids.com");
        adminUser.setPasswordHash(passwordEncoder.encode("password"));
        adminUser.setName("Admin Import Test");
        adminUser.setRole(Role.ADMIN);
        adminUser = userRepository.save(adminUser);
    }

    @Test
    void createImportJob_asAdmin_shouldReturn201() throws Exception {
        ImportJobRequest request = new ImportJobRequest(null, "OPEN_DATA_STATISTICS");

        mockMvc.perform(post("/api/v1/admin/import-jobs")
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
                .andExpect(jsonPath("$.status").value("PENDING"));
    }
}
