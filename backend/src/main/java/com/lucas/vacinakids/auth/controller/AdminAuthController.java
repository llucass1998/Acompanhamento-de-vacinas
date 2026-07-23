package com.lucas.vacinakids.auth.controller;

import com.lucas.vacinakids.auth.dto.LoginRequest;
import com.lucas.vacinakids.auth.security.UserDetailsImpl;
import com.lucas.vacinakids.user.entity.User;
import com.lucas.vacinakids.user.repository.UserRepository;
import com.lucas.vacinakids.shared.exception.BusinessException;
import com.lucas.vacinakids.user.service.AuditService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;

@RestController
@Tag(name = "Admin Auth", description = "Endpoints de autenticação estendida e gestão de conta admin")
@RequestMapping("/api/v1/admin/auth")
public class AdminAuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;

    public AdminAuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuditService auditService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditService = auditService;
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Forçar troca de senha no primeiro login ou alterar senha administrativa")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @Valid @RequestBody LoginRequest request) {
        User admin = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new BusinessException("User not found"));

        if (passwordEncoder.matches(request.password(), admin.getPasswordHash())) {
            throw new BusinessException("New password cannot be the same as the current password");
        }

        admin.setPasswordHash(passwordEncoder.encode(request.password()));
        admin.setMustChangePassword(false);
        admin.setLastPasswordChangeAt(Instant.now());
        
        userRepository.save(admin);
        
        auditService.logAdminAction(admin.getId(), "PASSWORD_CHANGED", "User", admin.getId(), null, null, "Admin password rotation");
        
        return ResponseEntity.ok().build();
    }
}
