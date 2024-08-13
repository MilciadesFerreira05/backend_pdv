package com.pdv.controllers;

import com.pdv.models.Product;
import com.pdv.services.FileStorageService;
import com.pdv.services.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(
            @ModelAttribute Product product,
            @RequestParam("file") MultipartFile file) {
        try {
            Product savedProduct = productService.save(product, file);
            return ResponseEntity.ok(savedProduct);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @ModelAttribute Product updatedProduct,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Optional<Product> existingProductOpt = productService.findById(id);
            if (existingProductOpt.isPresent()) {
                Product product = existingProductOpt.get();
                product.setCode(updatedProduct.getCode());
                product.setName(updatedProduct.getName());
                product.setDescription(updatedProduct.getDescription());
                product.setPrice(updatedProduct.getPrice());
                product.setStock(updatedProduct.getStock());
                product.setStockControl(updatedProduct.getStockControl());

                if (file != null && !file.isEmpty()) {
                    String fileName = fileStorageService.saveFile(file);
                    product.setImageUrl(fileName);
                }

                Product savedProduct = productService.save(product, file);
                return ResponseEntity.ok(savedProduct);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
