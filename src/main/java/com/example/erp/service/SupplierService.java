package com.example.erp.service;

import com.example.erp.dto.request.SupplierRequest;
import com.example.erp.dto.response.SupplierResponse;
import com.example.erp.entity.Supplier;
import com.example.erp.exception.ResourceNotFoundException;
import com.example.erp.mapper.ErpMapper;
import com.example.erp.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final ErpMapper mapper;

    public Page<SupplierResponse> getSuppliers(String search, Pageable pageable) {
        Page<Supplier> suppliers = StringUtils.hasText(search)
                ? supplierRepository.searchSuppliers(search, pageable)
                : supplierRepository.findAll(pageable);
        return suppliers.map(mapper::toSupplierResponse);
    }

    public SupplierResponse getSupplierById(Long id) {
        return mapper.toSupplierResponse(supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", id)));
    }

    @Transactional
    public SupplierResponse createSupplier(SupplierRequest request) {
        Supplier supplier = Supplier.builder()
                .name(request.getName())
                .contactPerson(request.getContactPerson())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .status(request.getStatus())
                .build();
        return mapper.toSupplierResponse(supplierRepository.save(supplier));
    }

    @Transactional
    public SupplierResponse updateSupplier(Long id, SupplierRequest request) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", id));
        supplier.setName(request.getName());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        supplier.setAddress(request.getAddress());
        supplier.setStatus(request.getStatus());
        return mapper.toSupplierResponse(supplierRepository.save(supplier));
    }

    @Transactional
    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) throw new ResourceNotFoundException("Supplier", id);
        supplierRepository.deleteById(id);
    }
}
