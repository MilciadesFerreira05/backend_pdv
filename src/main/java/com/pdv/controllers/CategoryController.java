package com.pdv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.pdv.models.Category;
import com.pdv.services.CategoryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.util.List;


@RestController
@RequestMapping("/api/categories")
@Validated
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @PreAuthorize("hasAuthority('Category.active')")
    public List<Category> getActive() {
        return categoryService.findActive();
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('Category.all')")
    public List<Category> getAll() {
        return categoryService.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Category.create')")
    public ResponseEntity<Category> create(@RequestBody Category category) {        
        Category savedCategory = categoryService.save(category);
        return ResponseEntity.ok(savedCategory);
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
    public ResponseEntity<Category> updateProduct(@PathVariable @Positive Long id,
                                                  @Valid @RequestBody Category updatedCategory) {
        return categoryService.findById(id)
                .map(category -> {
                    category.setName(updatedCategory.getName());
                    category.setDescription(updatedCategory.getDescription());
                    Category savedProduct = categoryService.save(category);
                    return ResponseEntity.ok(savedProduct);
                })
                .orElse(ResponseEntity.notFound().build());
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
