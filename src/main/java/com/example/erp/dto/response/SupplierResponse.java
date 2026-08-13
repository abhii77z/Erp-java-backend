package com.example.erp.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SupplierResponse {
    private Long id;
    private String name;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private BigDecimal totalPurchases;
    private BigDecimal outstanding;
    private String status;
    private LocalDateTime createdAt;
}
