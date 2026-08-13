package com.example.erp.dto.request;

import com.example.erp.entity.Gender;
import com.example.erp.entity.ItemStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter @Setter
public class ProductRequest {
    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "SKU is required")
    private String sku;

    private String barcode;

    @NotNull(message = "Category is required")
    private Long categoryId;

    @NotBlank(message = "Unit is required")
    private String unit;

    @NotNull(message = "Purchase price is required")
    @DecimalMin(value = "0.0", message = "Purchase price must be non-negative")
    private BigDecimal purchasePrice;

    @NotNull(message = "Selling price is required")
    @DecimalMin(value = "0.01", message = "Selling price must be greater than 0")
    private BigDecimal sellingPrice;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    private BigDecimal taxRate = BigDecimal.ZERO;

    @Min(0)
    private Integer stock = 0;

    @Min(0)
    private Integer minStock = 5;

    private String size;
    private String color;
    private String brand;
    private Gender gender;
    private String description;
    private String imageUrl;
    private ItemStatus status = ItemStatus.ACTIVE;
}
