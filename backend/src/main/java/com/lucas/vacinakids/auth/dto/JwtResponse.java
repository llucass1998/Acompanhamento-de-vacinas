package com.lucas.vacinakids.auth.dto;

public record JwtResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    int expiresIn,
    UserResponse user
) {}
