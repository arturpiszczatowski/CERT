package com.cryptocurrency.cryptoAPI.controller;

import com.cryptocurrency.cryptoAPI.model.Currency;
import com.cryptocurrency.cryptoAPI.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static com.cryptocurrency.cryptoAPI.util.ApiConstants.API_KEY;
import static com.cryptocurrency.cryptoAPI.util.ApiConstants.API_URL;

@RestController
@RequestMapping("/crypto")
public class CryptoController {

    @Autowired
    CurrencyRepository currencyRepository;

    @GetMapping
    public String test() {
        RestTemplate restTemplate = new RestTemplate();
        var res = restTemplate.getForObject(API_URL+"?api_key="+API_KEY, String.class);

        Currency currency = new Currency("BTC", 123.1123);
        currencyRepository.save(currency);

        var allCurrencies = currencyRepository.findAll();
        System.out.println(allCurrencies);

        return res;
    }
}
