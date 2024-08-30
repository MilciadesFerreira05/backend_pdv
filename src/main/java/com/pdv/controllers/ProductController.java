package com.pdv.controllers;

import com.pdv.models.Product;
import com.pdv.services.FileStorageService;
import com.pdv.services.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    @GetMapping
    @PreAuthorize("hasAuthority('Product.active')")
    public List<Product> getAllProducts() {
        return productService.findActive();
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('Product.all')")
    public List<Product> getAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('Product.read')")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Product.create')")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {

        Product savedProduct = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('Product.update')")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
                
        return ResponseEntity.ok( productService.update(product, id));
                
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Product.delete')")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id) {
        return productService.findById(id)
        .map(product -> {
            productService.delete(product);
            return ResponseEntity.noContent().build();
        })
        .orElse(ResponseEntity.notFound().build());
    }
}
