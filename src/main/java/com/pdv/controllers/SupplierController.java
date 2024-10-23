package com.pdv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.pdv.models.Supplier;
import com.pdv.services.SupplierService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/suppliers")
@Validated
public class SupplierController {


    @Autowired
    private SupplierService supplierService;

    @GetMapping
    @PreAuthorize("hasAuthority('Supplier.active')")
    public Page<Supplier> getActive(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "id", required = false) String sort,
        @RequestParam(defaultValue = "ASC", required = false) String direction,
        @RequestParam(required = false) String q
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort));

        if (q != null && q.length() > 0) {
            return supplierService.search(q, pageable);
        }

        return supplierService.findActive(pageable);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('Supplier.all')")
    public Page<Supplier> getAll(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "id", required = false) String sort,
        @RequestParam(defaultValue = "ASC", required = false) String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort));
        return supplierService.findAll(pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Supplier.create')")
    public ResponseEntity<Supplier> create(@RequestBody Supplier supplier) {        
        Supplier savedSupplier = supplierService.save(supplier);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSupplier);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('Supplier.read')")
    public ResponseEntity<Supplier> getById(@PathVariable @Positive Long id) {
        return supplierService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('Supplier.update')")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable @Positive Long id, @Valid @RequestBody Supplier supplier) {

        return ResponseEntity.ok(supplierService.update(supplier, id));

    }
    

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Supplier.delete')")
    public ResponseEntity<Object> delete(@PathVariable @Positive Long id) {
        return supplierService.findById(id)
                .map(supplier -> {
                    supplierService.delete(supplier);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
