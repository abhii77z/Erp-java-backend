package com.example.erp.service;

import com.example.erp.dto.request.EmployeeRequest;
import com.example.erp.dto.response.EmployeeResponse;
import com.example.erp.entity.Employee;
import com.example.erp.exception.DuplicateResourceException;
import com.example.erp.exception.ResourceNotFoundException;
import com.example.erp.mapper.ErpMapper;
import com.example.erp.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ErpMapper mapper;

    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(mapper::toEmployeeResponse)
                .collect(Collectors.toList());
    }

    public EmployeeResponse getEmployeeById(Long id) {
        return mapper.toEmployeeResponse(employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id)));
    }

    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        if (employeeRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new DuplicateResourceException("Employee ID already exists: " + request.getEmployeeId());
        }
        Employee employee = Employee.builder()
                .name(request.getName())
                .employeeId(request.getEmployeeId())
                .department(request.getDepartment())
                .phone(request.getPhone())
                .role(request.getRole())
                .status(request.getStatus())
                .build();
        return mapper.toEmployeeResponse(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));
        employee.setName(request.getName());
        employee.setDepartment(request.getDepartment());
        employee.setPhone(request.getPhone());
        employee.setRole(request.getRole());
        employee.setStatus(request.getStatus());
        return mapper.toEmployeeResponse(employeeRepository.save(employee));
    }
}
