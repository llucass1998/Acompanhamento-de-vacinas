package com.lucas.vacinakids.auth.controller;

import com.lucas.vacinakids.auth.dto.JwtResponse;
import com.lucas.vacinakids.auth.dto.LoginRequest;
import com.lucas.vacinakids.auth.dto.RegisterRequest;
import com.lucas.vacinakids.auth.dto.TokenRefreshRequest;
import com.lucas.vacinakids.auth.dto.UserResponse;
import com.lucas.vacinakids.auth.security.UserDetailsImpl;
import com.lucas.vacinakids.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Auth", description = "Endpoints de autenticação e gerenciamento de usuário")
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar novo usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Email já em uso")
    })
    public void register(@Valid @RequestBody RegisterRequest request) {
        authService.registerUser(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Login de usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login bem sucedido"),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renovar token de acesso usando refresh token")
    public ResponseEntity<JwtResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request.refreshToken()));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Logout do usuário (revoga tokens associados)")
    public void logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null) {
            authService.logout(userDetails.getId());
        }
    }

    @GetMapping("/me")
    @Operation(summary = "Obter dados do usuário logado")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(authService.getMe(userDetails.getId()));
    }
}
