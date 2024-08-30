package com.pdv.services;

import com.pdv.models.ProductPurchase;
import com.pdv.models.User;
import com.pdv.repositories.ProductPurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductPurchaseService extends BaseService<ProductPurchase> {

    @Autowired
    private ProductPurchaseRepository ProductpurchaseRepository;

    public ProductPurchaseService(){
        this.repository = ProductpurchaseRepository;
    }
  
    
    public ProductPurchase update(ProductPurchase purchase) {

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
