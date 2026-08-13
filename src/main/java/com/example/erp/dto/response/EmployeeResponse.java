package com.example.erp.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EmployeeResponse {
    private Long id;
    private String name;
    private String employeeId;
    private String department;
    private String phone;
    private String role;
    private String status;
    private LocalDateTime createdAt;
}
