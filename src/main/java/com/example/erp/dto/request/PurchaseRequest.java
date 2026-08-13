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
public class PurchaseRequest {
    @NotNull(message = "Supplier is required")
    private Long supplierId;

    @NotNull
    @Size(min = 1)
    @Valid
    private List<PurchaseItemRequest> items;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus = PaymentStatus.PAID;
    private LocalDate purchaseDate;
    private String notes;
}
