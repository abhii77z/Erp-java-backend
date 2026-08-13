package com.example.erp.repository;

import com.example.erp.entity.StockMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    @Query("SELECT sm FROM StockMovement sm LEFT JOIN FETCH sm.product WHERE sm.product.id = :productId ORDER BY sm.createdAt DESC")
    List<StockMovement> findByProductId(@Param("productId") Long productId);

    @Query("SELECT sm FROM StockMovement sm LEFT JOIN FETCH sm.product ORDER BY sm.createdAt DESC")
    Page<StockMovement> findAllWithProduct(Pageable pageable);
}
