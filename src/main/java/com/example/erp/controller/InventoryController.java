package com.example.erp.controller;

import com.example.erp.dto.request.StockAdjustmentRequest;
import com.example.erp.dto.response.StockMovementResponse;
import com.example.erp.service.InventoryService;
import com.example.erp.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Stock tracking and movement management")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/adjust")
    @Operation(summary = "Manually adjust product stock")
    public ResponseEntity<ApiResponse<StockMovementResponse>> adjustStock(
            @Valid @RequestBody StockAdjustmentRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Stock adjusted", inventoryService.adjustStock(request)));
    }

    @GetMapping("/movements")
    @Operation(summary = "Get all stock movements with pagination")
    public ResponseEntity<ApiResponse<Page<StockMovementResponse>>> getMovements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success("Stock movements retrieved",
                inventoryService.getMovements(pageable)));
    }

    @GetMapping("/movements/{productId}")
    @Operation(summary = "Get stock movements for a specific product")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getMovementsByProduct(
            @PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success("Product movements retrieved",
                inventoryService.getMovementsByProduct(productId)));
    }
}
