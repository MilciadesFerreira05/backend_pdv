package com.pdv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pdv.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}


