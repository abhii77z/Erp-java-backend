package com.example.erp.service;

import com.example.erp.dto.request.ProductRequest;
import com.example.erp.dto.response.ProductResponse;
import com.example.erp.entity.*;
import com.example.erp.exception.DuplicateResourceException;
import com.example.erp.exception.ResourceNotFoundException;
import com.example.erp.mapper.ErpMapper;
import com.example.erp.repository.CategoryRepository;
import com.example.erp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ErpMapper mapper;

    public Page<ProductResponse> getProducts(String search, Pageable pageable) {
        Page<Product> products = StringUtils.hasText(search)
                ? productRepository.searchProducts(search, pageable)
                : productRepository.findAllWithCategory(pageable);
        return products.map(mapper::toProductResponse);
    }

    public List<ProductResponse> getLowStockProducts() {
        return productRepository.findLowStockProducts(ItemStatus.ACTIVE)
                .stream().map(mapper::toProductResponse).collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        return mapper.toProductResponse(product);
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException("SKU already in use: " + request.getSku());
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));

        Product product = Product.builder()
                .name(request.getName())
                .sku(request.getSku())
                .barcode(request.getBarcode())
                .category(category)
                .unit(request.getUnit())
                .purchasePrice(request.getPurchasePrice())
                .sellingPrice(request.getSellingPrice())
                .taxRate(request.getTaxRate())
                .stock(request.getStock())
                .minStock(request.getMinStock())
                .size(request.getSize())
                .color(request.getColor())
                .brand(request.getBrand())
                .gender(request.getGender())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .status(request.getStatus())
                .build();

        return mapper.toProductResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));

        if (productRepository.existsBySkuAndIdNot(request.getSku(), id)) {
            throw new DuplicateResourceException("SKU already in use: " + request.getSku());
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));

        product.setName(request.getName());
        product.setSku(request.getSku());
        product.setBarcode(request.getBarcode());
        product.setCategory(category);
        product.setUnit(request.getUnit());
        product.setPurchasePrice(request.getPurchasePrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setTaxRate(request.getTaxRate());
        product.setMinStock(request.getMinStock());
        product.setSize(request.getSize());
        product.setColor(request.getColor());
        product.setBrand(request.getBrand());
        product.setGender(request.getGender());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());
        product.setStatus(request.getStatus());

        return mapper.toProductResponse(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", id);
        }
        productRepository.deleteById(id);
    }
}
