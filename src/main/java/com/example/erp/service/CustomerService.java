package com.example.erp.service;

import com.example.erp.dto.request.CustomerRequest;
import com.example.erp.dto.response.CustomerResponse;
import com.example.erp.entity.Customer;
import com.example.erp.exception.ResourceNotFoundException;
import com.example.erp.mapper.ErpMapper;
import com.example.erp.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ErpMapper mapper;

    public Page<CustomerResponse> getCustomers(String search, Pageable pageable) {
        Page<Customer> customers = StringUtils.hasText(search)
                ? customerRepository.searchCustomers(search, pageable)
                : customerRepository.findAll(pageable);
        return customers.map(mapper::toCustomerResponse);
    }

    public CustomerResponse getCustomerById(Long id) {
        return mapper.toCustomerResponse(customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id)));
    }

    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer customer = Customer.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .status(request.getStatus())
                .build();
        return mapper.toCustomerResponse(customerRepository.save(customer));
    }

    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        customer.setStatus(request.getStatus());
        return mapper.toCustomerResponse(customerRepository.save(customer));
    }

    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) throw new ResourceNotFoundException("Customer", id);
        customerRepository.deleteById(id);
    }
}
