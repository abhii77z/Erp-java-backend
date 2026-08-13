package com.example.erp.dto.request;

import com.example.erp.entity.ItemStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomerRequest {
    @NotBlank(message = "Customer name is required")
    private String name;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @Email(message = "Invalid email format")
    private String email;

    private String address;
    private ItemStatus status = ItemStatus.ACTIVE;
}
