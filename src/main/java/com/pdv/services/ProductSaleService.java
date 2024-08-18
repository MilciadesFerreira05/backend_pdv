package com.pdv.services;

import com.pdv.models.ProductSale;
import com.pdv.repositories.ProductSaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductSaleService {

    @Autowired
    private ProductSaleRepository ProductSaleRepository;

    @Autowired
    private UserInfoService userInfoService;

    public List<ProductSale> findActive() {
        return ProductSaleRepository.findByDeletedAtIsNull();
    }

    public List<ProductSale> findAll() {
        return ProductSaleRepository.findAll();
    }

    public Optional<ProductSale> findById(Long id) {
        return ProductSaleRepository.findById(id);
    }

    public ProductSale save(ProductSale sale) {
        sale.setCreatedBy(userInfoService.getCurrentUser());
        return ProductSaleRepository.save(sale);
    }

    public void delete(ProductSale sale) {
        sale.setDeletedBy(userInfoService.getCurrentUser());
        ProductSaleRepository.save(sale);
    }

}

