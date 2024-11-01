package com.pdv.controllers;

import com.pdv.models.Product;
import com.pdv.services.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    /**
     * Get all active products.
     *
     * @param page   the page number
     * @param size   the page size
     * @param sort   the field to sort on
     * @param direction the sort direction (ASC or DESC)
     * @return the page of products
     */
    @GetMapping
    @PreAuthorize("hasAuthority('Product.active')")
    public Page<Product> getAllProducts(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "id", required = false) String sort,
        @RequestParam(defaultValue = "ASC", required = false) String direction,
        @RequestParam(required = false) String q
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort));

        if (q != null && q.length() > 0) {
            return productService.search(q, pageable);
        }

        return productService.findActive(pageable);
    }

    
    /**
     * Search for products by name, description or code.
     *
     * @param q      the search query
     * @param page   the page number
     * @param size   the page size
     * @param sort   the field to sort on
     * @param direction the sort direction (ASC or DESC)
     * @return the page of products
     */
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

    @GetMapping("/code")
    @PreAuthorize("hasAuthority('Product.active')")
    public Optional<Product> searchByNameOrDescriptionOrCode(
        @RequestParam(required = true) String code
    ) {
        return productService.findByCode(code);
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

    @GetMapping("/report")
    @PreAuthorize("hasAuthority('Product.active')")
    public ResponseEntity<InputStreamResource> generateReport(@RequestParam HashMap<String, Object> parameters) {
        
        String report = parameters.get("report").toString();
        parameters.remove("report");

        ByteArrayInputStream bis = productService.generatePdfReport("reports/products/"+report+".jasper", parameters);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=report.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
