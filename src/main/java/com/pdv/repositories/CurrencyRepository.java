package com.pdv.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pdv.models.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
}
