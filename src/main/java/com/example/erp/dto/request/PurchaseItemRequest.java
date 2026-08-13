package com.example.erp.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter @Setter
public class PurchaseItemRequest {
    @NotNull
    private Long productId;

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal unitPrice;

    @DecimalMin("0.0")
    private BigDecimal discount = BigDecimal.ZERO;

    @DecimalMin("0.0")
    private BigDecimal tax = BigDecimal.ZERO;
}
