package com.pdv.controllers;



import java.io.ByteArrayInputStream;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pdv.models.ProductSale;
import com.pdv.services.ProductSaleService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/sales")
public class ProductSaleController {
    
    @Autowired
    private ProductSaleService saleService;

    @GetMapping
    @PreAuthorize("hasAuthority('ProductSale.active')")
    public Page<ProductSale> getActive(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "id", required = false) String sort,
        @RequestParam(defaultValue = "ASC", required = false) String direction,
        @RequestParam(required = false) String q
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort));

        if (q != null && q.length() > 0) {
            return saleService.search(q, pageable);
        }
        
        return saleService.findActive(pageable);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ProductSale.all')")
    public Page<ProductSale> getAll(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "id", required = false) String sort,
        @RequestParam(defaultValue = "ASC", required = false) String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort));
        return saleService.findAll(pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ProductSale.create')")
    public ResponseEntity<ProductSale> create(@RequestBody ProductSale productSale) {        
        ProductSale savedSale = saleService.save(productSale);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSale);
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
    public ResponseEntity<ProductSale> updateProductSale(@PathVariable @Positive Long id, @Valid @RequestBody ProductSale sale) {

        return ResponseEntity.ok(saleService.update(sale, id));
            
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

    @GetMapping("/report")
    @PreAuthorize("hasAuthority('Product.active')")
    public ResponseEntity<InputStreamResource> generateReport(@RequestParam HashMap<String, Object> parameters) {
        
        String report = parameters.get("report").toString();
        parameters.remove("report");

        ByteArrayInputStream bis = saleService.generatePdfReport("reports/sale/"+report+".jasper", parameters);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=report.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    
    @GetMapping("/print/{id}")
    @PreAuthorize("hasAuthority('ProductSale.active')")
    public ResponseEntity<Object> print(@PathVariable @Positive Long id) {
        return saleService.findById(id)
        .map(product -> {
            saleService.printSale(product);
            return ResponseEntity.noContent().build();
        })
        .orElse(ResponseEntity.notFound().build());
    }
}
