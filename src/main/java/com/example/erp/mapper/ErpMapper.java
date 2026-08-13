package com.example.erp.mapper;

import com.example.erp.dto.response.*;
import com.example.erp.entity.*;
import com.example.erp.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ErpMapper {

    // ── Category ──────────────────────────────────────────────────────────────
    public CategoryResponse toCategoryResponse(Category category, long productCount) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .productCount(productCount)
                .status(category.getStatus().name())
                .createdAt(category.getCreatedAt())
                .build();
    }

    public CategoryResponse toCategoryResponse(Category category) {
        return toCategoryResponse(category, 0L);
    }

    // ── Product ───────────────────────────────────────────────────────────────
    public ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .barcode(product.getBarcode())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .unit(product.getUnit())
                .purchasePrice(product.getPurchasePrice())
                .sellingPrice(product.getSellingPrice())
                .taxRate(product.getTaxRate())
                .stock(product.getStock())
                .minStock(product.getMinStock())
                .size(product.getSize())
                .color(product.getColor())
                .brand(product.getBrand())
                .gender(product.getGender() != null ? product.getGender().name() : null)
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .status(product.getStatus().name())
                .createdAt(product.getCreatedAt())
                .build();
    }

    // ── Customer ──────────────────────────────────────────────────────────────
    public CustomerResponse toCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .totalOrders(customer.getTotalOrders())
                .totalSpent(customer.getTotalSpent())
                .outstanding(customer.getOutstanding())
                .status(customer.getStatus().name())
                .createdAt(customer.getCreatedAt())
                .build();
    }

    // ── Supplier ──────────────────────────────────────────────────────────────
    public SupplierResponse toSupplierResponse(Supplier supplier) {
        return SupplierResponse.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .contactPerson(supplier.getContactPerson())
                .phone(supplier.getPhone())
                .email(supplier.getEmail())
                .address(supplier.getAddress())
                .totalPurchases(supplier.getTotalPurchases())
                .outstanding(supplier.getOutstanding())
                .status(supplier.getStatus().name())
                .createdAt(supplier.getCreatedAt())
                .build();
    }

    // ── SaleItem ──────────────────────────────────────────────────────────────
    public SaleItemResponse toSaleItemResponse(SaleItem item) {
        return SaleItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .discount(item.getDiscount())
                .tax(item.getTax())
                .subtotal(item.getSubtotal())
                .total(item.getTotal())
                .build();
    }

    // ── Sale ──────────────────────────────────────────────────────────────────
    public SaleResponse toSaleResponse(Sale sale) {
        return SaleResponse.builder()
                .id(sale.getId())
                .invoiceNumber(sale.getInvoiceNumber())
                .customerId(sale.getCustomer() != null ? sale.getCustomer().getId() : null)
                .customerName(sale.getCustomer() != null ? sale.getCustomer().getName() : "Walk-in")
                .items(sale.getItems().stream().map(this::toSaleItemResponse).collect(Collectors.toList()))
                .subtotal(sale.getSubtotal())
                .taxTotal(sale.getTaxTotal())
                .discountTotal(sale.getDiscountTotal())
                .total(sale.getTotal())
                .paymentMethod(sale.getPaymentMethod().name())
                .paymentStatus(sale.getPaymentStatus().name())
                .status(sale.getStatus().name())
                .notes(sale.getNotes())
                .saleDate(sale.getSaleDate())
                .createdAt(sale.getCreatedAt())
                .build();
    }

    // ── PurchaseItem ──────────────────────────────────────────────────────────
    public PurchaseItemResponse toPurchaseItemResponse(PurchaseItem item) {
        return PurchaseItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .discount(item.getDiscount())
                .tax(item.getTax())
                .subtotal(item.getSubtotal())
                .total(item.getTotal())
                .build();
    }

    // ── Purchase ──────────────────────────────────────────────────────────────
    public PurchaseResponse toPurchaseResponse(Purchase purchase) {
        return PurchaseResponse.builder()
                .id(purchase.getId())
                .referenceNumber(purchase.getReferenceNumber())
                .supplierId(purchase.getSupplier() != null ? purchase.getSupplier().getId() : null)
                .supplierName(purchase.getSupplier() != null ? purchase.getSupplier().getName() : null)
                .items(purchase.getItems().stream().map(this::toPurchaseItemResponse).collect(Collectors.toList()))
                .subtotal(purchase.getSubtotal())
                .taxTotal(purchase.getTaxTotal())
                .discountTotal(purchase.getDiscountTotal())
                .total(purchase.getTotal())
                .paymentMethod(purchase.getPaymentMethod().name())
                .paymentStatus(purchase.getPaymentStatus().name())
                .status(purchase.getStatus().name())
                .notes(purchase.getNotes())
                .purchaseDate(purchase.getPurchaseDate())
                .createdAt(purchase.getCreatedAt())
                .build();
    }

    // ── Expense ───────────────────────────────────────────────────────────────
    public ExpenseResponse toExpenseResponse(Expense expense) {
        return ExpenseResponse.builder()
                .id(expense.getId())
                .date(expense.getDate())
                .description(expense.getDescription())
                .category(expense.getCategory())
                .amount(expense.getAmount())
                .paymentMethod(expense.getPaymentMethod())
                .status(expense.getStatus().name())
                .createdAt(expense.getCreatedAt())
                .build();
    }

    // ── Employee ──────────────────────────────────────────────────────────────
    public EmployeeResponse toEmployeeResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .employeeId(employee.getEmployeeId())
                .department(employee.getDepartment())
                .phone(employee.getPhone())
                .role(employee.getRole().name())
                .status(employee.getStatus().name())
                .createdAt(employee.getCreatedAt())
                .build();
    }

    // ── StockMovement ──────────────────────────────────────────────────────────
    public StockMovementResponse toStockMovementResponse(StockMovement sm) {
        return StockMovementResponse.builder()
                .id(sm.getId())
                .productId(sm.getProduct().getId())
                .productName(sm.getProduct().getName())
                .type(sm.getType().name())
                .quantity(sm.getQuantity())
                .previousStock(sm.getPreviousStock())
                .newStock(sm.getNewStock())
                .referenceId(sm.getReferenceId())
                .notes(sm.getNotes())
                .createdAt(sm.getCreatedAt())
                .build();
    }
}
