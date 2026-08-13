package com.example.erp.service;

import com.example.erp.dto.request.StockAdjustmentRequest;
import com.example.erp.dto.response.StockMovementResponse;
import com.example.erp.entity.*;
import com.example.erp.exception.BusinessException;
import com.example.erp.exception.ResourceNotFoundException;
import com.example.erp.mapper.ErpMapper;
import com.example.erp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;
    private final UserRepository userRepository;
    private final ErpMapper mapper;

    @Transactional
    public StockMovementResponse adjustStock(StockAdjustmentRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", request.getProductId()));

        int previousStock = product.getStock();
        int newStock;

        if (request.getType() == StockMovementType.ADJUSTMENT) {
            // For ADJUSTMENT, quantity can be positive (add) or negative (reduce)
            newStock = previousStock + request.getQuantity();
        } else if (request.getType() == StockMovementType.RETURN) {
            newStock = previousStock + Math.abs(request.getQuantity());
        } else {
            throw new BusinessException("Only ADJUSTMENT and RETURN types are allowed for manual adjustments");
        }

        if (newStock < 0) {
            throw new BusinessException("Stock cannot go below zero. Current: " + previousStock);
        }

        product.setStock(newStock);
        productRepository.save(product);

        User user = getCurrentUser();
        StockMovement movement = StockMovement.builder()
                .product(product)
                .type(request.getType())
                .quantity(request.getQuantity())
                .previousStock(previousStock)
                .newStock(newStock)
                .notes(request.getNotes())
                .user(user)
                .build();

        return mapper.toStockMovementResponse(stockMovementRepository.save(movement));
    }

    public Page<StockMovementResponse> getMovements(Pageable pageable) {
        return stockMovementRepository.findAllWithProduct(pageable)
                .map(mapper::toStockMovementResponse);
    }

    public List<StockMovementResponse> getMovementsByProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product", productId);
        }
        return stockMovementRepository.findByProductId(productId)
                .stream().map(mapper::toStockMovementResponse).collect(Collectors.toList());
    }

    // Used internally by SaleService and PurchaseService
    @Transactional
    public void decrementStock(Product product, int quantity, String referenceId, User user) {
        int previousStock = product.getStock();
        int newStock = previousStock - quantity;
        if (newStock < 0) {
            throw new BusinessException("Insufficient stock for product: " + product.getName() +
                    ". Available: " + previousStock + ", Required: " + quantity);
        }
        product.setStock(newStock);
        productRepository.save(product);

        StockMovement movement = StockMovement.builder()
                .product(product)
                .type(StockMovementType.SALE)
                .quantity(-quantity)
                .previousStock(previousStock)
                .newStock(newStock)
                .referenceId(referenceId)
                .user(user)
                .build();
        stockMovementRepository.save(movement);
    }

    @Transactional
    public void incrementStock(Product product, int quantity, String referenceId, User user) {
        int previousStock = product.getStock();
        int newStock = previousStock + quantity;
        product.setStock(newStock);
        productRepository.save(product);

        StockMovement movement = StockMovement.builder()
                .product(product)
                .type(StockMovementType.PURCHASE)
                .quantity(quantity)
                .previousStock(previousStock)
                .newStock(newStock)
                .referenceId(referenceId)
                .user(user)
                .build();
        stockMovementRepository.save(movement);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElse(null);
    }
}
