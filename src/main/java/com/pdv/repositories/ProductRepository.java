package com.pdv.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pdv.models.Product;

@Repository
public interface ProductRepository extends BaseRepository<Product, Long> {
	@Query("SELECT p FROM Product p WHERE p.name LIKE %:q% OR p.description LIKE %:q% OR p.code LIKE %:q%")
	Page<Product> findByNameOrDescriptionOrCode(@Param("q") String q, Pageable pageable);
}


