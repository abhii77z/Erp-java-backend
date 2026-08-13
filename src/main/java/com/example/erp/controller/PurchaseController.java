package com.example.erp.controller;

import com.example.erp.dto.request.PurchaseRequest;
import com.example.erp.dto.response.PurchaseResponse;
import com.example.erp.service.PurchaseService;
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
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
@Tag(name = "Purchases", description = "Purchase orders - auto increments inventory")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PurchaseResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success("Purchases retrieved", purchaseService.getPurchases(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PurchaseResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Purchase retrieved", purchaseService.getPurchaseById(id)));
    }

    @PostMapping
    @Operation(summary = "Create a new purchase order (automatically increments product stock)")
    public ResponseEntity<ApiResponse<PurchaseResponse>> create(@Valid @RequestBody PurchaseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Purchase created", purchaseService.createPurchase(request)));
    }
}
