package com.pdv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pdv.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
