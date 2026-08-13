package com.example.erp.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private Long productCount;
    private String status;
    private LocalDateTime createdAt;
}
