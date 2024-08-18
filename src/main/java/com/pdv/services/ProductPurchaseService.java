package com.pdv.services;

import com.pdv.models.ProductPurchase;
import com.pdv.repositories.ProductPurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductPurchaseService {

    @Autowired
    private ProductPurchaseRepository ProductpurchaseRepository;

    @Autowired
    private UserInfoService userInfoService;


    public List<ProductPurchase> findActive() {
        return ProductpurchaseRepository.findByDeletedAtIsNull();
    }

    public List<ProductPurchase> findAll() {
        return ProductpurchaseRepository.findAll();
    }

    public Optional<ProductPurchase> findById(Long id) {
        return ProductpurchaseRepository.findById(id);
    }

    public ProductPurchase save(ProductPurchase purchase) {
        purchase.setCreatedBy(userInfoService.getCurrentUser());
        return ProductpurchaseRepository.save(purchase);
    }
    
    public void delete(ProductPurchase purchase) {
        purchase.setDeletedBy(userInfoService.getCurrentUser());
        ProductpurchaseRepository.save(purchase);
    }
    
}
