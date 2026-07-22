package com.lucas.vacinakids.auth.service;

import com.lucas.vacinakids.auth.dto.JwtResponse;
import com.lucas.vacinakids.auth.dto.LoginRequest;
import com.lucas.vacinakids.auth.dto.RegisterRequest;
import com.lucas.vacinakids.auth.dto.UserResponse;
import com.lucas.vacinakids.auth.entity.RefreshToken;
import com.lucas.vacinakids.auth.repository.RefreshTokenRepository;
import com.lucas.vacinakids.auth.security.JwtUtils;
import com.lucas.vacinakids.auth.security.UserDetailsImpl;
import com.lucas.vacinakids.shared.exception.BusinessException;
import com.lucas.vacinakids.shared.exception.ConflictException;
import com.lucas.vacinakids.shared.exception.NotFoundException;
import com.lucas.vacinakids.user.entity.Role;
import com.lucas.vacinakids.user.entity.User;
import com.lucas.vacinakids.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Value("${app.jwt.access-expiration-minutes}")
    private int jwtAccessExpirationMinutes;

    @Value("${app.jwt.refresh-expiration-days}")
    private int jwtRefreshExpirationDays;

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public void registerUser(RegisterRequest request) {
        String normalizedEmail = request.email().toLowerCase().trim();
        
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new ConflictException("Email is already in use");
        }

        User user = new User(
                request.name().trim(),
                normalizedEmail,
                passwordEncoder.encode(request.password()),
                Role.USER
        );

        userRepository.save(user);
    }

    @Transactional
    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email().toLowerCase().trim(), request.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        String jwt = jwtUtils.generateAccessToken(userDetails);

        String rawRefreshToken = UUID.randomUUID().toString();
        String tokenHash = hashToken(rawRefreshToken);

        RefreshToken refreshToken = new RefreshToken(
                user,
                tokenHash,
                Instant.now().plus(jwtRefreshExpirationDays, ChronoUnit.DAYS)
        );

        refreshTokenRepository.save(refreshToken);

        return new JwtResponse(
                jwt,
                rawRefreshToken,
                "Bearer",
                jwtAccessExpirationMinutes * 60,
                new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole())
        );
    }

    @Transactional
    public JwtResponse refreshToken(String requestRefreshToken) {
        String tokenHash = hashToken(requestRefreshToken);

        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new BusinessException("Refresh token is not in database!"));

        if (!refreshToken.isValid()) {
            throw new BusinessException("Refresh token was expired or revoked. Please make a new signin request");
        }

        User user = refreshToken.getUser();
        
        // Revoke the old token (rotation)
        refreshToken.revoke();
        refreshTokenRepository.save(refreshToken);

        // Generate new access token
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        String jwt = jwtUtils.generateAccessToken(userDetails);

        // Generate new refresh token
        String newRawRefreshToken = UUID.randomUUID().toString();
        String newTokenHash = hashToken(newRawRefreshToken);

        RefreshToken newRefreshToken = new RefreshToken(
                user,
                newTokenHash,
                Instant.now().plus(jwtRefreshExpirationDays, ChronoUnit.DAYS)
        );

        refreshTokenRepository.save(newRefreshToken);

        return new JwtResponse(
                jwt,
                newRawRefreshToken,
                "Bearer",
                jwtAccessExpirationMinutes * 60,
                new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole())
        );
    }

    @Transactional
    public void logout(UUID userId) {
        refreshTokenRepository.revokeAllUserTokens(userId);
    }

    public UserResponse getMe(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not hash token", e);
        }
    }
}
