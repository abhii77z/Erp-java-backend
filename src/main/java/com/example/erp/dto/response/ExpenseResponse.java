package com.example.erp.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ExpenseResponse {
    private Long id;
    private LocalDate date;
    private String description;
    private String category;
    private BigDecimal amount;
    private String paymentMethod;
    private String status;
    private LocalDateTime createdAt;
}
