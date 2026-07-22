package com.lucas.vacinakids.auth.dto;

import com.lucas.vacinakids.user.entity.Role;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String name,
    String email,
    Role role
) {}
