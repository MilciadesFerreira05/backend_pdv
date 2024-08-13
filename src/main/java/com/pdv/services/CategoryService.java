package com.pdv.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdv.models.Category;
import com.pdv.repositories.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserInfoService userInfoService;

    public List<Category> findAll() {
       return categoryRepository.findAll();
    }

    public List<Category> findActive() {
        return categoryRepository.findByDeletedAtIsNull(); 
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category save(Category category) {
        category.setCreatedBy(userInfoService.getCurrentUser());
        return categoryRepository.save(category);
    }

    public Category update(Category category, Long id) {
        Category currentCategory = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        currentCategory.setName(category.getName());
        currentCategory.setDescription(category.getDescription());
        currentCategory.setUpdatedBy(userInfoService.getCurrentUser());
        return categoryRepository.save(currentCategory);
    }

    public void delete(Category category) {
        category.setDeletedBy( userInfoService.getCurrentUser());
        categoryRepository.save(category);
    }

}
