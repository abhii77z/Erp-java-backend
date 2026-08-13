package com.example.erp.service;

import com.example.erp.dto.request.CategoryRequest;
import com.example.erp.dto.response.CategoryResponse;
import com.example.erp.entity.Category;
import com.example.erp.exception.DuplicateResourceException;
import com.example.erp.exception.ResourceNotFoundException;
import com.example.erp.mapper.ErpMapper;
import com.example.erp.repository.CategoryRepository;
import com.example.erp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ErpMapper mapper;

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(c -> mapper.toCategoryResponse(c, productRepository.countByCategoryId(c.getId())))
                .collect(Collectors.toList());
    }

    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
        return mapper.toCategoryResponse(category, productRepository.countByCategoryId(id));
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException("Category already exists: " + request.getName());
        }
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(request.getStatus())
                .build();
        return mapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));

        if (!category.getName().equalsIgnoreCase(request.getName()) &&
                categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException("Category name already in use: " + request.getName());
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setStatus(request.getStatus());
        return mapper.toCategoryResponse(categoryRepository.save(category),
                productRepository.countByCategoryId(id));
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category", id);
        }
        long count = productRepository.countByCategoryId(id);
        if (count > 0) {
            throw new com.example.erp.exception.BusinessException(
                "Cannot delete category with " + count + " products. Remove products first.");
        }
        categoryRepository.deleteById(id);
    }
}
