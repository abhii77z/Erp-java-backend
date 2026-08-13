package com.example.erp.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CustomerResponse {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private Integer totalOrders;
    private BigDecimal totalSpent;
    private BigDecimal outstanding;
    private String status;
    private LocalDateTime createdAt;
}
