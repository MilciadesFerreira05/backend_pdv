package com.pdv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.pdv.models.Supplier;
import com.pdv.services.SupplierService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.util.List;


@RestController
@RequestMapping("/api/suppliers")
@Validated
public class SupplierController {


    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public List<Supplier> getActive() {
        return supplierService.findActive();
    }

    @GetMapping("/all")
    public List<Supplier> getAll() {
        return supplierService.findAll();
    }

    @PostMapping
    public ResponseEntity<Supplier> create(@RequestBody Supplier supplier) {        
        Supplier savedSupplier = supplierService.save(supplier);
        return ResponseEntity.ok(savedSupplier);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getById(@PathVariable @Positive Long id) {
        return supplierService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable @Positive Long id,
                                                  @Valid @RequestBody Supplier updatedSupplier) {
        return supplierService.findById(id)
                .map(supplier -> {
                    supplier.setName(updatedSupplier.getName());
                    supplier.setAddress(updatedSupplier.getAddress());
                    Supplier savedSupplier = supplierService.save(supplier);
                    return ResponseEntity.ok(savedSupplier);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable @Positive Long id) {
        return supplierService.findById(id)
                .map(supplier -> {
                    supplierService.delete(supplier);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
