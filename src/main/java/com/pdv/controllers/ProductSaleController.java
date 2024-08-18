package com.pdv.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pdv.models.ProductSale;
import com.pdv.services.ProductSaleService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/sales")
public class ProductSaleController {
    
    @Autowired
    private ProductSaleService saleService;

    @GetMapping
    @PreAuthorize("hasAuthority('ProductSale.active')")
    public List<ProductSale> getActive() {
        return saleService.findActive();
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ProductSale.all')")
    public List<ProductSale> getAll() {
        return saleService.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ProductSale.create')")
    public ResponseEntity<ProductSale> create(@RequestBody ProductSale productSale) {        
        ProductSale savedSale = saleService.save(productSale);
        return ResponseEntity.ok(savedSale);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ProductSale.read')")
    public ResponseEntity<ProductSale> getById(@PathVariable @Positive Long id) {
        return saleService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ProductSale.update')")
    public ResponseEntity<ProductSale> updateProductSale(@PathVariable @Positive Long id,
                                                  @Valid @RequestBody ProductSale updatedProductSale) {
        return saleService.findById(id)
                .map( sale -> {
                    sale.setDate(updatedProductSale.getDate());
                    sale.setInvoiceNumber(updatedProductSale.getInvoiceNumber());
                    sale.setItems(updatedProductSale.getItems());
                    sale.setTotal(updatedProductSale.getTotal());
                    sale.setClient(updatedProductSale.getClient());

                    return ResponseEntity.ok(saleService.save(sale));
                }).orElse(ResponseEntity.notFound().build());
    }
    

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ProductSale.delete')")
    public ResponseEntity<Object> delete(@PathVariable @Positive Long id) {
        return saleService.findById(id)
                .map(product -> {
                    saleService.delete(product);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
