package com.example.erp.dto.request;

import com.example.erp.entity.StockMovementType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StockAdjustmentRequest {
    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotNull(message = "Movement type is required")
    private StockMovementType type;

    private String notes;
}
