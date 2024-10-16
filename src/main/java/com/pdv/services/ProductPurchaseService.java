package com.pdv.services;

import com.pdv.models.ProductPurchase;
import com.pdv.models.User;
import com.pdv.repositories.ProductPurchaseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductPurchaseService extends BaseService<ProductPurchase> {

    @Autowired
    private ProductPurchaseRepository ProductpurchaseRepository;

    public ProductPurchaseService(){
        this.repository = ProductpurchaseRepository;
    }

    @Transactional
    @Override
    public ProductPurchase save(ProductPurchase purchase) {
        User currentUser = this.userInfoService.getCurrentUser();
        purchase.setCreatedBy(currentUser);

        ProductPurchase savedPurchase = this.repository.save(purchase);

        String newProductPurchase = savedPurchase.toString();
        this.log("create", newProductPurchase, null, currentUser);

        return savedPurchase;
    }
    
    @Transactional
    @Override
    public ProductPurchase update(ProductPurchase purchase, Long id) {

        User currentUser = this.userInfoService.getCurrentUser();

        ProductPurchase purchaseFound = this.repository.findById(purchase.getId()).orElseThrow(() -> new RuntimeException("ProductPurchase not found"));

         String oldProductPurchase = purchaseFound.toString();

        purchaseFound.setDate(purchase.getDate());
        purchaseFound.setInvoiceNumber(purchase.getInvoiceNumber());
        purchaseFound.setItems(purchase.getItems());
        purchaseFound.setTotal(purchase.getTotal());
        purchaseFound.setSupplier(purchase.getSupplier());
        purchaseFound.setUpdatedBy(currentUser);
        
        ProductPurchase updatedPurchase = this.repository.save(purchaseFound);

         String newProductPurchase = updatedPurchase.toString();

         this.log("update", newProductPurchase, oldProductPurchase, currentUser);

        return updatedPurchase;
    }


}
