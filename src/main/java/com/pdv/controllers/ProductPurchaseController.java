package com.pdv.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pdv.models.Category;
import com.pdv.models.ProductPurchase;
import com.pdv.services.ProductPurchaseService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/purchases")
public class ProductPurchaseController {
    
    @Autowired
    private ProductPurchaseService purchaseService;

    @GetMapping
    public List<ProductPurchase> getActive() {
        return purchaseService.findActive();
    }

    @GetMapping("/all")
    public List<ProductPurchase> getAll() {
        return purchaseService.findAll();
    }

    @PostMapping
    public ResponseEntity<ProductPurchase> create(@RequestBody ProductPurchase productPurchase) {        
        ProductPurchase savedPurchase = purchaseService.save(productPurchase);
        return ResponseEntity.ok(savedPurchase);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductPurchase> getById(@PathVariable @Positive Long id) {
        return purchaseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<ProductPurchase> updateProductPurchase(@PathVariable @Positive Long id,
                                                  @Valid @RequestBody ProductPurchase updatedProductPurchase) {
        return purchaseService.findById(id)
                .map( purchase -> {
                    purchase.setDate(updatedProductPurchase.getDate());
                    purchase.setInvoiceNumber(updatedProductPurchase.getInvoiceNumber());
                    purchase.setItems(updatedProductPurchase.getItems());
                    purchase.setTotal(updatedProductPurchase.getTotal());
                    purchase.setSupplier(updatedProductPurchase.getSupplier());

                    return ResponseEntity.ok(purchaseService.save(purchase));
                }).orElse(ResponseEntity.notFound().build());
    }
    

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable @Positive Long id) {
        return categoryService.findById(id)
                .map(product -> {
                    categoryService.delete(product);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
