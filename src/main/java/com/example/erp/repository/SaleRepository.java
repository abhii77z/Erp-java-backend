package com.example.erp.repository;

import com.example.erp.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT s FROM Sale s LEFT JOIN FETCH s.customer WHERE " +
           "LOWER(s.invoiceNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.customer.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Sale> searchSales(@Param("search") String search, Pageable pageable);

    @Query("SELECT s FROM Sale s LEFT JOIN FETCH s.customer")
    Page<Sale> findAllWithCustomer(Pageable pageable);

    @Query("SELECT COALESCE(SUM(s.total), 0) FROM Sale s WHERE s.saleDate BETWEEN :from AND :to AND s.status <> 'CANCELLED'")
    BigDecimal sumTotalBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("SELECT s FROM Sale s LEFT JOIN FETCH s.customer LEFT JOIN FETCH s.items ORDER BY s.createdAt DESC")
    List<Sale> findTop5ByOrderByCreatedAtDesc(Pageable pageable);

    boolean existsByInvoiceNumber(String invoiceNumber);

    Optional<Sale> findByInvoiceNumber(String invoiceNumber);
}
