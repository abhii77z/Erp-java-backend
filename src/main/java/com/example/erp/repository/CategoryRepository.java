package com.example.erp.repository;

import com.example.erp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByNameIgnoreCase(String name);

    @Query("SELECT c FROM Category c LEFT JOIN Product p ON p.category.id = c.id " +
           "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Category> findByNameContainingIgnoreCase(String search);
}
