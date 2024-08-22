package com.pdv.services;

import com.pdv.models.Product;
import com.pdv.models.User;
import com.pdv.repositories.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserInfoService userInfoService; 

    @Autowired
    private FileStorageService fileStorageService;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findActive() {
        return productRepository.findByDeletedAtIsNull();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Product save(Product product){
        User currentUser = userInfoService.getCurrentUser();

        if (product.getId() == null) {
            product.setCreatedBy(currentUser);
        } else {
            product.setUpdatedBy(currentUser);
        }
        return productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.findById(id).ifPresent(product -> {
            product.setDeletedBy(userInfoService.getCurrentUser());
            productRepository.save(product);
        });
    }
}
