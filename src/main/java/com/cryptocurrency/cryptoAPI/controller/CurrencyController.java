package com.cryptocurrency.cryptoAPI.controller;

import com.cryptocurrency.cryptoAPI.model.Currency;
import com.cryptocurrency.cryptoAPI.service.CrudService;
import com.cryptocurrency.cryptoAPI.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@RestController
public class CurrencyController extends CrudController<Currency> {
    @Autowired
    public CurrencyController(CrudService<Currency> service) {
        super(service);
    }

    @GetMapping("/calc/{symbol}")
    public ResponseEntity<HashMap<String, Double>> getPrice(@PathVariable("symbol") String symbol){
        var calc = ((CurrencyService)service).calcValueFor(symbol);
        return new ResponseEntity<>(calc, HttpStatus.OK);
    }

    public Function<Currency, Map<String, Object>> transformToDTO() {
        return currency -> {
            var payload = new LinkedHashMap<String, Object>();
            payload.put("id", currency.getId());
            payload.put("symbol", currency.getSymbol());
            payload.put("value", currency.getValue());
            payload.put("user", currency.getUser().getUsername());

            return payload;
        };
    }
}
