package com.cryptocurrency.cryptoAPI.service;

import com.cryptocurrency.cryptoAPI.model.Currency;
import com.cryptocurrency.cryptoAPI.model.User;
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
        User user = findUserByUsername(username);

        double currentFunds = user.getMoney();
        var intendedBuy = calculatePrice(symbol, amount);
        boolean sufficientFunds = currentFunds > intendedBuy;
        if (!sufficientFunds)
            return false;

        user.setMoney(currentFunds - intendedBuy);
        Currency currency = findCurrency(symbol);
        addCurrency(username, currency, amount);
        repository.save(user);
        return true;
    }

    public boolean sell(String username, String symbol, int amount) {
        User user = findUserByUsername(username);

        var currentCurrencies = user.getCurrencies();

        var intendedSell = calculatePrice(symbol, amount);

        var amountOfAvailableCurrencies = user.getCurrencies().stream()
                .filter(currencies -> currencies.getSymbol().equals(symbol))
                .count();

        if(amountOfAvailableCurrencies < amount)
            return false;

        removeCurrency(username, symbol, (int) amountOfAvailableCurrencies);
        return true;
    }

    protected User findUserByUsername(String username) {
        var user = repository.findAll().stream()
                .filter(users -> users.getUsername().equals(username))
                .findAny()
                .orElse(null);
        return user;
    }

    protected Double calculatePrice(String symbol, int amount) {
        double price = currencyService.calcValueFor(symbol).get(Double.class) * amount;
        return price;
    }

    protected Currency findCurrency(String symbol){
        Currency currency = currencyService.getAll().stream()
                .filter(currencies -> currencies.getSymbol().equals(symbol))
                .findAny()
                .orElse(null);
        return currency;
    }

    protected void addCurrency(String username, Currency currency, int amount) {
        User user = findUserByUsername(username);
        var updatedCurrencies = user.getCurrencies();
        while (amount > 0) {
            updatedCurrencies.add(currency);
            amount--;
        }
        user.setCurrencies(updatedCurrencies);
        repository.save(user);
    }

    protected void removeCurrency(String username, String symbol, int amount) {
        User user = findUserByUsername(username);
        var updatedCurrencies = user.getCurrencies();
        while (amount > 0) {
            updatedCurrencies.remove(findCurrency(symbol));
            amount--;
        }
        user.setCurrencies(updatedCurrencies);
        repository.save(user);
    }
}
