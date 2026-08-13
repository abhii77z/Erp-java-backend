package com.example.erp.controller;

import com.example.erp.dto.request.EmployeeRequest;
import com.example.erp.dto.response.EmployeeResponse;
import com.example.erp.service.EmployeeService;
import com.example.erp.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Employees retrieved", employeeService.getAllEmployees()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Employee retrieved", employeeService.getEmployeeById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeResponse>> create(@Valid @RequestBody EmployeeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Employee created", employeeService.createEmployee(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponse>> update(@PathVariable Long id, @Valid @RequestBody EmployeeRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Employee updated", employeeService.updateEmployee(id, request)));
    }
}
