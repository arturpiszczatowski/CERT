package com.cryptocurrency.cryptoAPI.service;

import com.cryptocurrency.cryptoAPI.model.Currency;
import com.cryptocurrency.cryptoAPI.model.User;
import com.cryptocurrency.cryptoAPI.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends CrudService<User> {
    CurrencyService currencyService;

    @Autowired
    CurrencyRepository currencyRepository;

    public UserService(JpaRepository<User, Long> repository, CurrencyService currencyService) {
        super(repository);
        this.currencyService = currencyService;
        User brodo = new User("BrodoFagins","123", 1000000.0); //Temporary richboi
        repository.save(brodo);

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

        Currency currency = new Currency(symbol, currencyService.calcValueFor(symbol).get(symbol), user);
        if(currency == null)
            return false;

        addCurrency(username, currency, amount);
        user.setMoney(currentFunds - intendedBuy);
        repository.save(user);
        return true;
    }

    public boolean sell(String username, String symbol, int amount) {
        User user = findUserByUsername(username);

        var currentCurrencies = user.getCurrencies();

        var intendedSell = calculatePrice(symbol, amount);

        var amountOfAvailableCurrencies = currentCurrencies.stream()
                .filter(currencies -> currencies.getSymbol().equals(symbol))
                .count();

        if(amountOfAvailableCurrencies < amount)
            return false;

        removeCurrency(username, symbol, (int) amountOfAvailableCurrencies);
        user.setMoney(user.getMoney() + intendedSell);
        repository.save(user);
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
        double price = currencyService.calcValueFor(symbol).get(symbol) * amount;
        return price;
    }


    protected void addCurrency(String username, Currency currency, int amount) {
        User user = findUserByUsername(username);
        var updatedCurrencies = user.getCurrencies();
        while (amount > 0) {
            updatedCurrencies.add(currency);
            amount--;
        }
        user.setCurrencies(updatedCurrencies);
        currencyRepository.saveAll(user.getCurrencies());
    }

    protected void removeCurrency(String username, String symbol, int amount) {
        User user = findUserByUsername(username);
        var updatedCurrencies = user.getCurrencies();
        while (amount > 0) {
            updatedCurrencies.remove(currencyService.findBySymbol(symbol));
            amount--;
        }
        user.setCurrencies(updatedCurrencies);
        currencyRepository.saveAll(user.getCurrencies());
    }
}
