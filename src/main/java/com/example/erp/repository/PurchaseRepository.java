package com.example.erp.repository;

import com.example.erp.entity.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query("SELECT p FROM Purchase p LEFT JOIN FETCH p.supplier")
    Page<Purchase> findAllWithSupplier(Pageable pageable);

    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Purchase p WHERE p.purchaseDate BETWEEN :from AND :to AND p.status <> 'CANCELLED'")
    BigDecimal sumTotalBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);

    boolean existsByReferenceNumber(String referenceNumber);
}
