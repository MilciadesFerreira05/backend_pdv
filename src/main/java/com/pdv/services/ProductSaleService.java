package com.pdv.services;

import com.pdv.models.Product;
import com.pdv.models.ProductSale;
import com.pdv.models.ProductSaleItem;
import com.pdv.models.User;
import com.pdv.repositories.ProductSaleRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class ProductSaleService extends BaseService<ProductSale> {

    @Autowired
    private ProductSaleRepository ProductSaleRepository;

    ProductSaleService() {
        this.repository = ProductSaleRepository;
        this.columns = List.of("date", "invoiceNumber", "total");
        this.relatedEntity = "client";
        this.relatedColumns = List.of("name");
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
        saleFound.setTotal(sale.getTotal());
        saleFound.setClient(sale.getClient());
        saleFound.setUpdatedBy(currentUser);
    

        List<ProductSaleItem> existingItems = saleFound.getItems();

        existingItems.removeIf(item -> !sale.getItems().contains(item));
        for (ProductSaleItem newItem : sale.getItems()) {
            if (existingItems.contains(newItem)) {
                ProductSaleItem existingItem = existingItems.get(existingItems.indexOf(newItem));
                existingItem.setQuantity(newItem.getQuantity());
                existingItem.setPrice(newItem.getPrice());
                existingItem.setSubtotal(newItem.getSubtotal());
            } else {
                newItem.setSale(saleFound);
                existingItems.add(newItem);
            }
        }
    
        ProductSale updatedSale = this.repository.save(saleFound);
    
        String newProductSale = updatedSale.toString();
    
        this.log("update", newProductSale, oldProductSale, currentUser);
    
        return updatedSale;
    }
    


}

