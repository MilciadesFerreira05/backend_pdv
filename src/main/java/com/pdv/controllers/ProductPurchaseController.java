package com.pdv.controllers;


import java.io.ByteArrayInputStream;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;

import com.pdv.models.ProductPurchase;
import com.pdv.services.ProductPurchaseService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/purchases")
public class ProductPurchaseController {
    
    @Autowired
    private ProductPurchaseService purchaseService;

    @GetMapping
    @PreAuthorize("hasAuthority('ProductPurchase.active')")
    public Page<ProductPurchase> getActive(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "id", required = false) String sort,
        @RequestParam(defaultValue = "ASC", required = false) String direction,
        @RequestParam(required = false) String q
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort));

        if (q != null && q.length() > 0) {
            return purchaseService.search(q, pageable);
        }
        
        return purchaseService.findActive(pageable);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ProductPurchase.all')")
    public Page<ProductPurchase> getAll(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "id", required = false) String sort,
        @RequestParam(defaultValue = "ASC", required = false) String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort));
        return purchaseService.findAll(pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ProductPurchase.create')")
    public ResponseEntity<ProductPurchase> create(@RequestBody ProductPurchase productPurchase) {        
        ProductPurchase savedPurchase = purchaseService.save(productPurchase);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPurchase);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ProductPurchase.read')")
    public ResponseEntity<ProductPurchase> getById(@PathVariable @Positive Long id) {
        return purchaseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ProductPurchase.update')")
    public ResponseEntity<ProductPurchase> updateProductPurchase(@PathVariable @Positive Long id, @Valid @RequestBody ProductPurchase purchase) {
        return ResponseEntity.ok(purchaseService.update(purchase, id));
    }
    

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ProductPurchase.delete')")
    public ResponseEntity<Object> delete(@PathVariable @Positive Long id) {
        return purchaseService.findById(id)
                .map(product -> {
                    purchaseService.delete(product);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/report")
    @PreAuthorize("hasAuthority('ProductPurchase.active')")
    public ResponseEntity<InputStreamResource> generateReport(@RequestParam Date desde, @RequestParam Date hasta ) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("desde", desde);
        parameters.put("hasta", hasta); 
        ByteArrayInputStream bis = purchaseService.generatePdfReport("reports/purchase/purchases.jasper", parameters);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=report.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

}
