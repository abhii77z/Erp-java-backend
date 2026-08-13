package com.example.erp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "login_history")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoginHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String ipAddress;

    private String userAgent;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime loginTime;
}
