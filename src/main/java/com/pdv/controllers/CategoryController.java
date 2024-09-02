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

import com.pdv.models.Category;
import com.pdv.services.CategoryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;


@RestController
@RequestMapping("/api/categories")
@Validated
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @PreAuthorize("hasAuthority('Category.active')")
    public Page<Category> getActive( 
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "id", required = false) String sort,
        @RequestParam(defaultValue = "ASC", required = false) String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        return categoryService.findActive(pageable);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('Category.all')")
    public Page<Category> getAll(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "id", required = false) String sort,
        @RequestParam(defaultValue = "ASC", required = false) String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        return categoryService.findAll(pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Category.create')")
    public ResponseEntity<Category> create(@RequestBody Category category) {        
        Category savedCategory = categoryService.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('Category.read')")
    public ResponseEntity<Category> getById(@PathVariable @Positive Long id) {
        return categoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('Category.update')")
    public ResponseEntity<Category> updateProduct(@PathVariable @Positive Long id, @Valid @RequestBody Category updatedCategory) {

        return ResponseEntity.ok(categoryService.update(updatedCategory, id));
    
    }
    

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Category.delete')")
    public ResponseEntity<Object> delete(@PathVariable @Positive Long id) {
        return categoryService.findById(id)
                .map(product -> {
                    categoryService.delete(product);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
