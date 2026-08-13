package com.example.erp.service;

import com.example.erp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SaleRepository saleRepository;
    private final PurchaseRepository purchaseRepository;
    private final ExpenseRepository expenseRepository;
    private final ProductRepository productRepository;

    public Map<String, Object> getSalesReport(LocalDate from, LocalDate to) {
        BigDecimal total = saleRepository.sumTotalBetween(from, to);
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("period", Map.of("from", from.toString(), "to", to.toString()));
        report.put("totalSales", total);
        report.put("salesCount", saleRepository.count());
        return report;
    }

    public Map<String, Object> getPurchaseReport(LocalDate from, LocalDate to) {
        BigDecimal total = purchaseRepository.sumTotalBetween(from, to);
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("period", Map.of("from", from.toString(), "to", to.toString()));
        report.put("totalPurchases", total);
        return report;
    }

    public Map<String, Object> getInventoryReport() {
        long totalProducts = productRepository.count();
        long lowStockCount = productRepository.findLowStockProducts(
                com.example.erp.entity.ItemStatus.ACTIVE).size();
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("totalProducts", totalProducts);
        report.put("lowStockProducts", lowStockCount);
        return report;
    }

    public Map<String, Object> getExpenseReport(LocalDate from, LocalDate to) {
        BigDecimal total = expenseRepository.sumAmountBetween(from, to);
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("period", Map.of("from", from.toString(), "to", to.toString()));
        report.put("totalExpenses", total);
        return report;
    }

    public Map<String, Object> getProfitLossReport(LocalDate from, LocalDate to) {
        BigDecimal revenue = saleRepository.sumTotalBetween(from, to);
        BigDecimal purchases = purchaseRepository.sumTotalBetween(from, to);
        BigDecimal expenses = expenseRepository.sumAmountBetween(from, to);
        BigDecimal grossProfit = revenue.subtract(purchases);
        BigDecimal netProfit = grossProfit.subtract(expenses);

        Map<String, Object> report = new LinkedHashMap<>();
        report.put("period", Map.of("from", from.toString(), "to", to.toString()));
        report.put("revenue", revenue);
        report.put("costOfGoods", purchases);
        report.put("grossProfit", grossProfit);
        report.put("expenses", expenses);
        report.put("netProfit", netProfit);
        return report;
    }
}
