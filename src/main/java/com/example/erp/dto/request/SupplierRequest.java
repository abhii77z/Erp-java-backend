package com.example.erp.dto.request;

import com.example.erp.entity.ItemStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SupplierRequest {
    @NotBlank(message = "Supplier name is required")
    private String name;

    @NotBlank(message = "Contact person is required")
    private String contactPerson;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @Email(message = "Invalid email format")
    private String email;

    private String address;
    private ItemStatus status = ItemStatus.ACTIVE;
}
