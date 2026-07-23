package com.lucas.vacinakids.user.service;

import com.lucas.vacinakids.user.entity.Role;
import com.lucas.vacinakids.user.entity.User;
import com.lucas.vacinakids.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminBootstrapService {

    private static final Logger logger = LoggerFactory.getLogger(AdminBootstrapService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${bootstrap.admin.enabled:false}")
    private boolean bootstrapEnabled;

    @Value("${bootstrap.admin.email:}")
    private String bootstrapEmail;

    @Value("${bootstrap.admin.name:System Administrator}")
    private String bootstrapName;

    @Value("${bootstrap.admin.password:}")
    private String bootstrapPassword;

    public AdminBootstrapService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void bootstrapAdmin() {
        if (!bootstrapEnabled) {
            logger.debug("Admin bootstrap is disabled.");
            return;
        }

        if (bootstrapEmail == null || bootstrapEmail.isBlank() || bootstrapPassword == null || bootstrapPassword.isBlank()) {
            logger.warn("Admin bootstrap is enabled but email or password properties are missing. Skipping.");
            return;
        }

        String normalizedEmail = bootstrapEmail.toLowerCase().trim();

        // Check if ANY admin already exists
        boolean adminExists = userRepository.existsByRole(Role.ADMIN);
        if (adminExists) {
            logger.info("Admin user already exists in the system. Skipping bootstrap.");
            return;
        }

        logger.info("No ADMIN found in the system. Bootstrapping initial admin account for email: {}", normalizedEmail);

        User admin = new User(
                bootstrapName.trim(),
                normalizedEmail,
                passwordEncoder.encode(bootstrapPassword),
                Role.ADMIN
        );
        
        // Force password change on first login
        admin.setMustChangePassword(true);

        userRepository.save(admin);

        logger.info("Admin account successfully created. IMPORTANT: Please disable BOOTSTRAP_ADMIN_ENABLED in your environment variables.");
    }
}
