package com.example.erp.service;

import com.example.erp.dto.request.ExpenseRequest;
import com.example.erp.dto.response.ExpenseResponse;
import com.example.erp.entity.Expense;
import com.example.erp.entity.User;
import com.example.erp.exception.ResourceNotFoundException;
import com.example.erp.mapper.ErpMapper;
import com.example.erp.repository.ExpenseRepository;
import com.example.erp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final ErpMapper mapper;

    public Page<ExpenseResponse> getExpenses(Pageable pageable) {
        return expenseRepository.findAllByOrderByDateDesc(pageable)
                .map(mapper::toExpenseResponse);
    }

    public ExpenseResponse getExpenseById(Long id) {
        return mapper.toExpenseResponse(expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense", id)));
    }

    @Transactional
    public ExpenseResponse createExpense(ExpenseRequest request) {
        User currentUser = getCurrentUser();
        Expense expense = Expense.builder()
                .date(request.getDate())
                .description(request.getDescription())
                .category(request.getCategory())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .status(request.getStatus())
                .createdBy(currentUser)
                .build();
        return mapper.toExpenseResponse(expenseRepository.save(expense));
    }

    @Transactional
    public ExpenseResponse updateExpense(Long id, ExpenseRequest request) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense", id));
        expense.setDate(request.getDate());
        expense.setDescription(request.getDescription());
        expense.setCategory(request.getCategory());
        expense.setAmount(request.getAmount());
        expense.setPaymentMethod(request.getPaymentMethod());
        expense.setStatus(request.getStatus());
        return mapper.toExpenseResponse(expenseRepository.save(expense));
    }

    @Transactional
    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) throw new ResourceNotFoundException("Expense", id);
        expenseRepository.deleteById(id);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElse(null);
    }
}
