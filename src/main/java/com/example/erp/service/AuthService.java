package com.example.erp.service;

import com.example.erp.dto.request.LoginRequest;
import com.example.erp.dto.request.RegisterRequest;
import com.example.erp.dto.response.AuthResponse;
import com.example.erp.entity.LoginHistory;
import com.example.erp.entity.User;
import com.example.erp.entity.UserCredential;
import com.example.erp.exception.DuplicateResourceException;
import com.example.erp.exception.ResourceNotFoundException;
import com.example.erp.repository.LoginHistoryRepository;
import com.example.erp.repository.UserRepository;
import com.example.erp.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final LoginHistoryRepository loginHistoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getCredential().getPassword())) {
            logLoginHistory(user, httpRequest, "FAILED");
            throw new BadCredentialsException("Invalid email or password");
        }

        if (user.getStatus() == com.example.erp.entity.ItemStatus.PENDING) {
            logLoginHistory(user, httpRequest, "FAILED_UNVERIFIED");
            throw new BadCredentialsException("Account is not verified yet. Please wait for admin approval.");
        }

        if (user.getStatus() == com.example.erp.entity.ItemStatus.INACTIVE) {
            logLoginHistory(user, httpRequest, "FAILED_INACTIVE");
            throw new BadCredentialsException("Account is deactivated.");
        }

        logLoginHistory(user, httpRequest, "SUCCESS");

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    private void logLoginHistory(User user, HttpServletRequest httpRequest, String status) {
        LoginHistory history = LoginHistory.builder()
                .user(user)
                .ipAddress(httpRequest.getRemoteAddr())
                .userAgent(httpRequest.getHeader("User-Agent"))
                .status(status)
                .loginTime(LocalDateTime.now())
                .build();
        loginHistoryRepository.save(history);
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + request.getEmail());
        }

        boolean isFirstUser = userRepository.count() == 0;

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .role(isFirstUser ? com.example.erp.entity.Role.ADMIN : request.getRole())
                .status(isFirstUser ? com.example.erp.entity.ItemStatus.ACTIVE : com.example.erp.entity.ItemStatus.PENDING)
                .build();

        UserCredential credential = UserCredential.builder()
                .password(passwordEncoder.encode(request.getPassword()))
                .user(user)
                .build();
        user.setCredential(credential);

        User saved = userRepository.save(user);
        String token = jwtUtil.generateToken(saved.getEmail(), saved.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .role(saved.getRole().name())
                .build();
    }

    public AuthResponse getMe(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
