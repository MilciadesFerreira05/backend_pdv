package com.pdv.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdv.models.Category;
import com.pdv.models.Log;
import com.pdv.repositories.CategoryRepository;
import com.pdv.repositories.LogRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private LogRepository logRepository;

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
        System.out.println(userInfoService.getCurrentUser());
        Category savedCategory = categoryRepository.save(category);
        new Thread(()->{
            Log log = new Log();
            log.setAction("create");
            log.setDate(LocalDateTime.now());
            log.setNewData(category.toString());
            log.setUser(userInfoService.getCurrentUser());
            logRepository.save(log);
        }).start();
        return savedCategory;
    }

    public Category update(Category category, Long id) {
        Category currentCategory = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        currentCategory.setName(category.getName());
        currentCategory.setDescription(category.getDescription());
        currentCategory.setUpdatedBy(userInfoService.getCurrentUser());

        Category savedCategory = categoryRepository.save(currentCategory);
        new Thread(()->{
            Log log = new Log();
            log.setAction("update");
            log.setDate(LocalDateTime.now());
            log.setNewData(currentCategory.toString());
            log.setOldData(currentCategory.toString());
            log.setUser(userInfoService.getCurrentUser());
            logRepository.save(log);
        }).start();

        return savedCategory;
    }

    public void delete(Category category) {
        category.setDeletedBy( userInfoService.getCurrentUser());

        new Thread(()->{
            Log log = new Log();
            log.setAction("delete");
            log.setDate(LocalDateTime.now());
            log.setOldData(category.toString());
            log.setUser(userInfoService.getCurrentUser());
            logRepository.save(log);
        }).start();

        categoryRepository.save(category);
    }

}
