package com.pdv.services;

import com.pdv.models.Product;
import com.pdv.models.ProductSale;
import com.pdv.models.ProductSaleItem;
import com.pdv.models.User;
import com.pdv.repositories.ProductSaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class ProductSaleService extends BaseService<ProductSale> {

    @Autowired
    private ProductSaleRepository ProductSaleRepository;

    ProductSaleService() {
        this.repository = ProductSaleRepository;
    }

    @Transactional
    @Override
    public ProductSale save(ProductSale sale) {
        User currentUser = this.userInfoService.getCurrentUser();
        sale.setCreatedBy(currentUser);

        ProductSale savedSale = this.repository.save(sale);

        String newProductSale = savedSale.toString();
        this.log("create", newProductSale, null, currentUser);

        return savedSale;
    }


    @Override
    public ProductSale update(ProductSale sale, Long id) {

        User currentUser = this.userInfoService.getCurrentUser();

        ProductSale saleFound = this.repository.findById(id).orElseThrow(() -> new RuntimeException("ProductSale not found"));

        String oldProductSale = saleFound.toString();

        saleFound.setDate(sale.getDate());
        saleFound.setInvoiceNumber(sale.getInvoiceNumber());
        saleFound.setItems(sale.getItems());
        saleFound.setTotal(sale.getTotal());
        saleFound.setClient(sale.getClient());
        saleFound.setUpdatedBy(currentUser);
        
        ProductSale updatedPurchase = this.repository.save(saleFound);

        String newProductPurchase = updatedPurchase.toString();

        this.log("update", newProductPurchase, oldProductSale, currentUser);

        return updatedPurchase;
    }


}

