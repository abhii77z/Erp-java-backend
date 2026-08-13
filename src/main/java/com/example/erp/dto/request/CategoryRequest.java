package com.example.erp.dto.request;

import com.example.erp.entity.ItemStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CategoryRequest {
    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100)
    private String name;

    private String description;
    private ItemStatus status = ItemStatus.ACTIVE;
}
