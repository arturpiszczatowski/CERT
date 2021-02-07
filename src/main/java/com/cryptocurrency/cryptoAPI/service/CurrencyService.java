package com.cryptocurrency.cryptoAPI.service;

import com.cryptocurrency.cryptoAPI.model.Currency;
import com.cryptocurrency.cryptoAPI.repository.CurrencyRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cryptocurrency.cryptoAPI.util.ApiConstants.API_URL;
import static com.cryptocurrency.cryptoAPI.util.ApiConstants.CURRENCY;

@Service
public class CurrencyService extends CrudService<Currency>{

    public CurrencyService(JpaRepository<Currency, Long> repository) {
        super(repository);
    }

    @Override
    public Currency createOrUpdate(Currency updateEntity) {
        return null;
    }

    public HashMap<String, Double> calcValueFor(String symbol){
        HashMap<String, Double> result = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();
        var res = restTemplate.getForObject(API_URL+"data/price?tsyms=" + CURRENCY+"&fsym="+symbol, HashMap.class);
        result.put(symbol, (Double) res.get(CURRENCY));
        return result;
    }

    public Currency findBySymbol(String symbol) {
        var currency = ((CurrencyRepository)repository).findBySymbol(symbol);
        if(currency.isEmpty())
            return null;
        return currency.get();
    }

    public Map<String, Long> findByUsername(String username){
        var currency = repository.findAll().stream()
                .filter(c -> c.getUser().getUsername().equals(username))
                .map(c -> c.getSymbol())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return currency;
    }
}
