package com.example.erp.repository;

import com.example.erp.entity.ItemStatus;
import com.example.erp.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsBySku(String sku);
    boolean existsBySkuAndIdNot(String sku, Long id);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.sku) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Product> searchProducts(@Param("search") String search, Pageable pageable);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category")
    Page<Product> findAllWithCategory(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.stock <= p.minStock AND p.status = :status")
    List<Product> findLowStockProducts(@Param("status") ItemStatus status);

    long countByCategoryId(Long categoryId);
}
