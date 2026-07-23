package com.lucas.vacinakids.user.service;

import com.lucas.vacinakids.user.entity.AdminAuditLog;
import com.lucas.vacinakids.user.entity.User;
import com.lucas.vacinakids.user.repository.AdminAuditLogRepository;
import com.lucas.vacinakids.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuditService {

    private final AdminAuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public AuditService(AdminAuditLogRepository auditLogRepository, UserRepository userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAdminAction(UUID adminId, String action, String entityType, UUID entityId, String beforeData, String afterData, String reason) {
        User admin = null;
        if (adminId != null) {
            admin = userRepository.findById(adminId).orElse(null);
        }
        
        AdminAuditLog log = new AdminAuditLog(admin, action, entityType, entityId, beforeData, afterData, reason);
        auditLogRepository.save(log);
    }
}
