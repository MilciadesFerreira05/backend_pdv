package com.pdv.controllers;

import com.pdv.models.Product;
import com.pdv.services.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    @GetMapping
    @PreAuthorize("hasAuthority('Product.active')")
    public Page<Product> getAllProducts(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "id", required = false) String sort,
        @RequestParam(defaultValue = "ASC", required = false) String direction
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort));
        return productService.findActive(pageable);
    }

    
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('Product.active')")
    public Page<Product> searchByNameOrDescriptionOrCode(
        @RequestParam(defaultValue = "", required = false) String q,
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "id", required = false) String sort,
        @RequestParam(defaultValue = "ASC", required = false) String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort));
        return productService.findByNameOrDescriptionOrCode(q, pageable);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('Product.all')")
    public Page<Product> getAll(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "id", required = false) String sort,
        @RequestParam(defaultValue = "ASC", required = false) String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort));
        return productService.findAll(pageable);
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
