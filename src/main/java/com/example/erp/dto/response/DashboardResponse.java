package com.example.erp.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DashboardResponse {
    private BigDecimal totalSales;
    private BigDecimal totalPurchases;
    private BigDecimal revenue;
    private BigDecimal netProfit;
    private List<SaleResponse> recentSales;
    private List<ProductResponse> lowStockProducts;
    private List<Map<String, Object>> salesData;
}
