package com.pdv.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.pdv.models.Category;
import com.pdv.repositories.CategoryRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.util.List;


@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:5175")
@Validated
public class CategoryController {


    private CategoryRepository categoryRepository;

    // @Autowired
    // private UserService userService;

    public CategoryController(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category category) {        
        Category savedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(savedCategory);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable @Positive @RequestParam Long id) {
        return categoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateProduct(@PathVariable @Positive @RequestParam Long id,
                                                  @Valid @RequestBody Category updatedCategory) {
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setName(updatedCategory.getName());
                    category.setDescription(updatedCategory.getDescription());
                    Category savedProduct = categoryRepository.save(category);
                    return ResponseEntity.ok(savedProduct);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable @Positive @RequestParam Long id) {
        return categoryRepository.findById(id)
                .map(product -> {
                    categoryRepository.delete(product);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
