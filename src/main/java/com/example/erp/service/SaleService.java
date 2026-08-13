package com.example.erp.service;

import com.example.erp.dto.request.SaleRequest;
import com.example.erp.dto.request.SaleItemRequest;
import com.example.erp.dto.response.SaleResponse;
import com.example.erp.entity.*;
import com.example.erp.exception.ResourceNotFoundException;
import com.example.erp.mapper.ErpMapper;
import com.example.erp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final InventoryService inventoryService;
    private final ErpMapper mapper;

    public Page<SaleResponse> getSales(String search, Pageable pageable) {
        Page<Sale> sales = StringUtils.hasText(search)
                ? saleRepository.searchSales(search, pageable)
                : saleRepository.findAllWithCustomer(pageable);
        return sales.map(mapper::toSaleResponse);
    }

    public SaleResponse getSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", id));
        return mapper.toSaleResponse(sale);
    }

    @Transactional
    public SaleResponse createSale(SaleRequest request) {
        User currentUser = getCurrentUser();

        // Resolve customer if provided
        Customer customer = null;
        if (request.getCustomerId() != null) {
            customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", request.getCustomerId()));
        }

        // Generate invoice number
        String invoiceNumber = generateInvoiceNumber();

        Sale sale = Sale.builder()
                .invoiceNumber(invoiceNumber)
                .customer(customer)
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(request.getPaymentStatus())
                .status(SaleStatus.COMPLETED)
                .notes(request.getNotes())
                .saleDate(request.getSaleDate() != null ? request.getSaleDate() : LocalDate.now())
                .createdBy(currentUser)
                .items(new ArrayList<>())
                .build();

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal taxTotal = BigDecimal.ZERO;
        BigDecimal discountTotal = BigDecimal.ZERO;

        for (SaleItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", itemReq.getProductId()));

            // Decrement inventory
            inventoryService.decrementStock(product, itemReq.getQuantity(), invoiceNumber, currentUser);

            BigDecimal itemSubtotal = itemReq.getUnitPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            BigDecimal itemDiscount = itemReq.getDiscount() != null ? itemReq.getDiscount() : BigDecimal.ZERO;
            BigDecimal taxRate = product.getTaxRate();
            BigDecimal taxableAmount = itemSubtotal.subtract(itemDiscount);
            BigDecimal itemTax = taxableAmount.multiply(taxRate).divide(BigDecimal.valueOf(100));
            BigDecimal itemTotal = taxableAmount.add(itemTax);

            SaleItem saleItem = SaleItem.builder()
                    .sale(sale)
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(itemReq.getUnitPrice())
                    .discount(itemDiscount)
                    .tax(itemTax)
                    .subtotal(itemSubtotal)
                    .total(itemTotal)
                    .build();

            sale.getItems().add(saleItem);
            subtotal = subtotal.add(itemSubtotal);
            taxTotal = taxTotal.add(itemTax);
            discountTotal = discountTotal.add(itemDiscount);
        }

        BigDecimal total = subtotal.subtract(discountTotal).add(taxTotal);
        sale.setSubtotal(subtotal);
        sale.setTaxTotal(taxTotal);
        sale.setDiscountTotal(discountTotal);
        sale.setTotal(total);

        // Update customer stats
        if (customer != null) {
            customer.setTotalOrders(customer.getTotalOrders() + 1);
            customer.setTotalSpent(customer.getTotalSpent().add(total));
            if (request.getPaymentStatus() == PaymentStatus.PARTIAL) {
                customer.setOutstanding(customer.getOutstanding().add(total.multiply(BigDecimal.valueOf(0.5))));
            } else if (request.getPaymentStatus() == PaymentStatus.UNPAID) {
                customer.setOutstanding(customer.getOutstanding().add(total));
            }
            customerRepository.save(customer);
        }

        return mapper.toSaleResponse(saleRepository.save(sale));
    }

    public List<SaleResponse> getRecentSales(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("createdAt").descending());
        return saleRepository.findAllWithCustomer(pageable).map(mapper::toSaleResponse).getContent();
    }

    private String generateInvoiceNumber() {
        String prefix = "INV-" + LocalDate.now().getYear() + "-";
        long count = saleRepository.count() + 1;
        return prefix + String.format("%04d", count);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElse(null);
    }
}
