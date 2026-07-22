package com.lucas.vacinakids.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TokenRefreshRequest(
    @NotBlank(message = "Refresh token is required")
    @Size(max = 512, message = "Refresh token is too long")
    String refreshToken
) {}
