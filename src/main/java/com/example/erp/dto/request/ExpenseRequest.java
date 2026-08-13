package com.example.erp.dto.request;

import com.example.erp.entity.ExpenseStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
public class ExpenseRequest {
    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    private String paymentMethod;
    private ExpenseStatus status = ExpenseStatus.PAID;
}
