package com.pdv.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pdv.models.Product;

@Repository
public interface ProductRepository extends BaseRepository<Product, Long> {
	@Query("SELECT p FROM Product p WHERE p.name LIKE %:q% OR p.code LIKE %:q%")
	List<Product> findByNameOrCode(@Param("q") String q);
}


