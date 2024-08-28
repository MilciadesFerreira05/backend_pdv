package com.pdv.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdv.models.Supplier;
import com.pdv.repositories.SupplierRepository;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private UserInfoService userInfoService;

    public List<Supplier> findAll() {
       return supplierRepository.findAll();
    }

    public List<Supplier> findActive() {
        return supplierRepository.findByDeletedAtIsNull(); 
    }

    public Optional<Supplier> findById(Long id) {
        return supplierRepository.findById(id);
    }

    public Supplier save(Supplier supplier) {
        supplier.setCreatedBy(userInfoService.getCurrentUser());
        return supplierRepository.save(supplier);
    }

    public Supplier update(Supplier supplier, Long id) {
        Supplier currentSupplier = supplierRepository.findById(id).orElseThrow(() -> new RuntimeException("Supplier not found"));
        currentSupplier.setName(supplier.getName());
        currentSupplier.setContactName(supplier.getContactName());
        currentSupplier.setEmail(supplier.getEmail());
        currentSupplier.setPhone(supplier.getPhone());
        currentSupplier.setAddress(supplier.getAddress());
        currentSupplier.setUpdatedBy(userInfoService.getCurrentUser());
        return supplierRepository.save(currentSupplier);
    }
    public void delete(Supplier supplier) {
        supplier.setDeletedBy(userInfoService.getCurrentUser());
        supplierRepository.save(supplier);
    }

}

