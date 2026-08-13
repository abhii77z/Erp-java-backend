package com.example.erp.service;

import com.example.erp.dto.response.DashboardResponse;
import com.example.erp.dto.response.SaleResponse;
import com.example.erp.dto.response.ProductResponse;
import com.example.erp.entity.ItemStatus;
import com.example.erp.mapper.ErpMapper;
import com.example.erp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final SaleRepository saleRepository;
    private final PurchaseRepository purchaseRepository;
    private final ExpenseRepository expenseRepository;
    private final ProductRepository productRepository;
    private final ErpMapper mapper;

    public DashboardResponse getDashboardSummary() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.withDayOfMonth(1); // First day of current month

        BigDecimal totalSales = saleRepository.sumTotalBetween(startDate, endDate);
        BigDecimal totalPurchases = purchaseRepository.sumTotalBetween(startDate, endDate);
        BigDecimal totalExpenses = expenseRepository.sumAmountBetween(startDate, endDate);
        BigDecimal netProfit = totalSales.subtract(totalPurchases).subtract(totalExpenses);

        // Recent sales (last 5)
        List<SaleResponse> recentSales = saleRepository.findAllWithCustomer(
                PageRequest.of(0, 5, Sort.by("createdAt").descending()))
                .map(mapper::toSaleResponse)
                .getContent();

        // Low stock products
        List<ProductResponse> lowStockProducts = productRepository
                .findLowStockProducts(ItemStatus.ACTIVE)
                .stream().map(mapper::toProductResponse).collect(Collectors.toList());

        // Sales data for last 7 days
        List<Map<String, Object>> salesData = buildWeeklySalesData();

        return DashboardResponse.builder()
                .totalSales(totalSales)
                .totalPurchases(totalPurchases)
                .revenue(totalSales)
                .netProfit(netProfit)
                .recentSales(recentSales)
                .lowStockProducts(lowStockProducts)
                .salesData(salesData)
                .build();
    }

    private List<Map<String, Object>> buildWeeklySalesData() {
        List<Map<String, Object>> result = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            BigDecimal salesAmount = saleRepository.sumTotalBetween(date, date);
            BigDecimal purchasesAmount = purchaseRepository.sumTotalBetween(date, date);

            Map<String, Object> dayData = new LinkedHashMap<>();
            dayData.put("name", date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
            dayData.put("sales", salesAmount);
            dayData.put("purchases", purchasesAmount);
            result.add(dayData);
        }
        return result;
    }
}
