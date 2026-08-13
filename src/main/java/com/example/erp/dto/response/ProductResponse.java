package com.example.erp.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String sku;
    private String barcode;
    private Long categoryId;
    private String categoryName;
    private String unit;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private BigDecimal taxRate;
    private Integer stock;
    private Integer minStock;
    private String size;
    private String color;
    private String brand;
    private String gender;
    private String description;
    private String imageUrl;
    private String status;
    private LocalDateTime createdAt;
}
