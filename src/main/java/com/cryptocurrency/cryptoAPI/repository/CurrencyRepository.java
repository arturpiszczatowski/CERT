package com.cryptocurrency.cryptoAPI.repository;

import com.cryptocurrency.cryptoAPI.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Optional<Currency> findBySymbol(String symbol);
}
