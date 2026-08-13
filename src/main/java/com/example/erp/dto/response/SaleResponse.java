package com.example.erp.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SaleResponse {
    private Long id;
    private String invoiceNumber;
    private Long customerId;
    private String customerName;
    private List<SaleItemResponse> items;
    private BigDecimal subtotal;
    private BigDecimal taxTotal;
    private BigDecimal discountTotal;
    private BigDecimal total;
    private String paymentMethod;
    private String paymentStatus;
    private String status;
    private String notes;
    private LocalDate saleDate;
    private LocalDateTime createdAt;
}
