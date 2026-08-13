package com.example.erp.controller;

import com.example.erp.dto.request.SaleRequest;
import com.example.erp.dto.response.SaleResponse;
import com.example.erp.service.SaleService;
import com.example.erp.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
@Tag(name = "Sales", description = "Sales transactions - auto decrements inventory")
public class SaleController {

    private final SaleService saleService;

    @GetMapping
    @Operation(summary = "Get paginated sales with optional search by invoice or customer name")
    public ResponseEntity<ApiResponse<Page<SaleResponse>>> getAll(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success("Sales retrieved", saleService.getSales(search, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SaleResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Sale retrieved", saleService.getSaleById(id)));
    }

    @PostMapping
    @Operation(summary = "Create a new sale (automatically decrements product stock)")
    public ResponseEntity<ApiResponse<SaleResponse>> create(@Valid @RequestBody SaleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Sale created", saleService.createSale(request)));
    }
}
