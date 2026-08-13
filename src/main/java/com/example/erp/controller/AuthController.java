package com.example.erp.controller;

import com.example.erp.dto.request.LoginRequest;
import com.example.erp.dto.request.RegisterRequest;
import com.example.erp.dto.response.AuthResponse;
import com.example.erp.service.AuthService;
import com.example.erp.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login, register and user info endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login with email and password")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        AuthResponse auth = authService.login(request, httpRequest);
        return ResponseEntity.ok(ApiResponse.success("Login successful", auth));
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse auth = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", auth));
    }

    @GetMapping("/me")
    @Operation(summary = "Get currently logged-in user info")
    public ResponseEntity<ApiResponse<AuthResponse>> getMe(@AuthenticationPrincipal UserDetails userDetails) {
        AuthResponse auth = authService.getMe(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", auth));
    }
}
