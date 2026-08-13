package com.example.erp.controller;

import com.example.erp.service.ReportService;
import com.example.erp.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "Business report generation endpoints")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/sales")
    @Operation(summary = "Sales report for a date range")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSalesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(ApiResponse.success("Sales report generated", reportService.getSalesReport(from, to)));
    }

    @GetMapping("/purchases")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPurchaseReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(ApiResponse.success("Purchase report generated", reportService.getPurchaseReport(from, to)));
    }

    @GetMapping("/inventory")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getInventoryReport() {
        return ResponseEntity.ok(ApiResponse.success("Inventory report generated", reportService.getInventoryReport()));
    }

    @GetMapping("/expenses")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getExpenseReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(ApiResponse.success("Expense report generated", reportService.getExpenseReport(from, to)));
    }

    @GetMapping("/profit-loss")
    @Operation(summary = "Profit and Loss report for a date range")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProfitLoss(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(ApiResponse.success("P&L report generated", reportService.getProfitLossReport(from, to)));
    }
}
