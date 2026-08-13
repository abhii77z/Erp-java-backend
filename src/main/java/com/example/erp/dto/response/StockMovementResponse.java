package com.example.erp.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StockMovementResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String type;
    private Integer quantity;
    private Integer previousStock;
    private Integer newStock;
    private String referenceId;
    private String notes;
    private LocalDateTime createdAt;
}
