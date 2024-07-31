package com.pdv.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pdv.models.Currency;
import com.pdv.repositories.CurrencyRepository;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {

    private CurrencyRepository currencyRepository;

    public CurrencyController(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @GetMapping
    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Currency> getCurrencyById(@PathVariable Long id) {
        Optional<Currency> currency = currencyRepository.findById(id);
        if (currency.isPresent()) {
            return ResponseEntity.ok(currency.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Currency createCurrency(@RequestBody Currency currency) {
        return currencyRepository.save(currency);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Currency> updateCurrency(@PathVariable Long id, @RequestBody Currency currencyDetails) {
        Optional<Currency> currency = currencyRepository.findById(id);
        if (currency.isPresent()) {
            Currency updatedCurrency = currency.get();
            updatedCurrency.setCode(currencyDetails.getCode());
            updatedCurrency.setName(currencyDetails.getName());
            updatedCurrency.setSymbol(currencyDetails.getSymbol());
            currencyRepository.save(updatedCurrency);
            return ResponseEntity.ok(updatedCurrency);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable Long id) {
        if (currencyRepository.findById(id).isPresent()) {
            currencyRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
