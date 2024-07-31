package com.pdv.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdv.models.Currency;
import com.pdv.repositories.CurrencyRepository;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    public List<Currency> findAll() {
        return currencyRepository.findAll();
    }

    public Currency findById(Long id) {
        return currencyRepository.findById(id).orElse(null);
    }

    public Currency save(Currency currency) {
        return currencyRepository.save(currency);
    }

    public void deleteById(Long id) {
        currencyRepository.deleteById(id);
    }
}
