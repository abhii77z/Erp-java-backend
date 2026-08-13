package com.example.erp.service;

import com.example.erp.dto.request.PurchaseRequest;
import com.example.erp.dto.request.PurchaseItemRequest;
import com.example.erp.dto.response.PurchaseResponse;
import com.example.erp.entity.*;
import com.example.erp.exception.ResourceNotFoundException;
import com.example.erp.mapper.ErpMapper;
import com.example.erp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final InventoryService inventoryService;
    private final ErpMapper mapper;

    public Page<PurchaseResponse> getPurchases(Pageable pageable) {
        return purchaseRepository.findAllWithSupplier(pageable).map(mapper::toPurchaseResponse);
    }

    public PurchaseResponse getPurchaseById(Long id) {
        return mapper.toPurchaseResponse(purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase", id)));
    }

    @Transactional
    public PurchaseResponse createPurchase(PurchaseRequest request) {
        User currentUser = getCurrentUser();

        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", request.getSupplierId()));

        String referenceNumber = generateReferenceNumber();

        Purchase purchase = Purchase.builder()
                .referenceNumber(referenceNumber)
                .supplier(supplier)
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(request.getPaymentStatus())
                .status(PurchaseStatus.RECEIVED)
                .notes(request.getNotes())
                .purchaseDate(request.getPurchaseDate() != null ? request.getPurchaseDate() : LocalDate.now())
                .createdBy(currentUser)
                .items(new ArrayList<>())
                .build();

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal taxTotal = BigDecimal.ZERO;
        BigDecimal discountTotal = BigDecimal.ZERO;

        for (PurchaseItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", itemReq.getProductId()));

            // Increment inventory
            inventoryService.incrementStock(product, itemReq.getQuantity(), referenceNumber, currentUser);

            BigDecimal itemSubtotal = itemReq.getUnitPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            BigDecimal itemDiscount = itemReq.getDiscount() != null ? itemReq.getDiscount() : BigDecimal.ZERO;
            BigDecimal itemTax = itemReq.getTax() != null ? itemReq.getTax() : BigDecimal.ZERO;
            BigDecimal itemTotal = itemSubtotal.subtract(itemDiscount).add(itemTax);

            PurchaseItem purchaseItem = PurchaseItem.builder()
                    .purchase(purchase)
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(itemReq.getUnitPrice())
                    .discount(itemDiscount)
                    .tax(itemTax)
                    .subtotal(itemSubtotal)
                    .total(itemTotal)
                    .build();

            purchase.getItems().add(purchaseItem);
            subtotal = subtotal.add(itemSubtotal);
            taxTotal = taxTotal.add(itemTax);
            discountTotal = discountTotal.add(itemDiscount);
        }

        BigDecimal total = subtotal.subtract(discountTotal).add(taxTotal);
        purchase.setSubtotal(subtotal);
        purchase.setTaxTotal(taxTotal);
        purchase.setDiscountTotal(discountTotal);
        purchase.setTotal(total);

        // Update supplier stats
        supplier.setTotalPurchases(supplier.getTotalPurchases().add(total));
        if (request.getPaymentStatus() == PaymentStatus.UNPAID) {
            supplier.setOutstanding(supplier.getOutstanding().add(total));
        }
        supplierRepository.save(supplier);

        return mapper.toPurchaseResponse(purchaseRepository.save(purchase));
    }

    private String generateReferenceNumber() {
        long count = purchaseRepository.count() + 1;
        return "PO-" + LocalDate.now().getYear() + "-" + String.format("%04d", count);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElse(null);
    }
}
