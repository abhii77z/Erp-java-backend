package com.example.erp.repository;

import com.example.erp.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Page<Expense> findAllByOrderByDateDesc(Pageable pageable);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.date BETWEEN :from AND :to AND e.status = 'PAID'")
    BigDecimal sumAmountBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
