package com.pdv.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pdv.models.ProductSale;

@Repository
public interface ProductSaleRepository extends JpaRepository<ProductSale, Long> {
    List<ProductSale> findByDeletedAtIsNull();
}


