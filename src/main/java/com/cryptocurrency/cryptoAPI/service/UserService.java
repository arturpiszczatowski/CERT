package com.cryptocurrency.cryptoAPI.service;

import com.cryptocurrency.cryptoAPI.model.User;
import com.cryptocurrency.cryptoAPI.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends CrudService<User> {
    CurrencyService currencyService;

    public UserService(JpaRepository<User, Long> repository, CurrencyService currencyService) {
        super(repository);
        this.currencyService = currencyService;
    }

    @Override
    public User createOrUpdate(User updateEntity) {
        return null;
    }

    public boolean buy(String username, String symbol, int amount) {
        var user = repository.findAll().stream()
                .filter(users -> users.getUsername().equals(username))
                .findAny()
                .orElse(null);

        var intendedBuy = currencyService.calcValueFor(symbol).get(Double.class) * amount;

        boolean sufficientFunds = user.getMoney() > intendedBuy;

        if(sufficientFunds)
            return true;

        return false;
    }
}
