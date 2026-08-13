package com.example.erp.controller;

import com.example.erp.dto.response.DashboardResponse;
import com.example.erp.service.DashboardService;
import com.example.erp.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    @Operation(summary = "Get dashboard summary data (sales, purchases, profit, low stock, chart data)")
    public ResponseEntity<ApiResponse<DashboardResponse>> getSummary() {
        return ResponseEntity.ok(ApiResponse.success("Dashboard data retrieved", dashboardService.getDashboardSummary()));
    }
}
