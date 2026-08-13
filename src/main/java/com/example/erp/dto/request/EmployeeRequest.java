package com.example.erp.dto.request;

import com.example.erp.entity.ItemStatus;
import com.example.erp.entity.Role;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmployeeRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Employee ID is required")
    private String employeeId;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotNull(message = "Role is required")
    private Role role;

    private ItemStatus status = ItemStatus.ACTIVE;
}
