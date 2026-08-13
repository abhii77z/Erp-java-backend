package com.example.erp.dto.request;

import com.example.erp.entity.PaymentMethod;
import com.example.erp.entity.PaymentStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class SaleRequest {
    private Long customerId;

    @NotNull(message = "At least one item is required")
    @Size(min = 1)
    @Valid
    private List<SaleItemRequest> items;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus = PaymentStatus.PAID;
    private LocalDate saleDate;
    private String notes;
}
