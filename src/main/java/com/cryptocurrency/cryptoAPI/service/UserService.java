package com.cryptocurrency.cryptoAPI.service;

import com.cryptocurrency.cryptoAPI.model.Currency;
import com.cryptocurrency.cryptoAPI.model.User;
import com.cryptocurrency.cryptoAPI.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService extends CrudService<User> implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    CurrencyService currencyService;

    @Autowired
    CurrencyRepository currencyRepository;

    public UserService(JpaRepository<User, Long> repository, CurrencyService currencyService) {
        super(repository);
        this.currencyService = currencyService;
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public boolean buy(String username, String symbol, int amount) {
        User user = loadUserByUsername(username);

        double currentFunds = user.getMoney();
        var intendedBuy = calculatePrice(symbol, amount);
        boolean sufficientFunds = currentFunds > intendedBuy;
        if (!sufficientFunds)
            return false;

        Currency currency = new Currency(symbol, currencyService.calcValueFor(symbol).get(symbol), user);
        //NUM07-J. Do not attempt comparisons with NaN
        if(currency == null)
            return false;

        //NUM09-J. Do not use floating-point variables as loop counters
        for(float i=amount; i>0f; i--)
        addCurrency(username, currency);
        user.setMoney(currentFunds - intendedBuy);
        repository.save(user);
        return true;
    }

    public boolean sell(String username, String symbol, int amount) {
        User user = loadUserByUsername(username);

        var currentCurrencies = user.getCurrencies();

        var intendedSell = calculatePrice(symbol, amount);

        var amountOfAvailableCurrencies = currentCurrencies.stream()
                .filter(currencies -> currencies.getSymbol().equals(symbol))
                .count();

        if(amountOfAvailableCurrencies < amount)
            return false;

        var sellingCurrencies = currentCurrencies.stream()
                .filter(currencies -> currencies.getSymbol().equals(symbol))
                .limit(amount)
                .collect(Collectors.toList());

        removeCurrency(username, sellingCurrencies);
        user.setMoney(user.getMoney() + intendedSell);
        repository.save(user);
        return true;
    }


    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = repository.findAll().stream()
                .filter(users -> users.getUsername().equals(username))
                .findAny()
                .orElse(null);
        return foundUser;
    }

    @Override
    public User createOrUpdate(User user) {
        if(loadUserByUsername(user.getUsername()) == null){
            GrantedAuthority defaultAuthority = () -> "ROLE_USER";
            String encodedPassword = passwordEncoder.encode(user.getPassword());

            user.setPassword(encodedPassword);
            user.addAuthority(defaultAuthority);

            return repository.save(user);
        } else {
            var foundUser = loadUserByUsername(user.getUsername());
            String updatedEncodedPassword = passwordEncoder.encode(user.getPassword());

            foundUser.setPassword(updatedEncodedPassword);
            foundUser.setAuthority(user.getAuthority());

            return repository.save(foundUser);
        }
    }

    protected Double calculatePrice(String symbol, int amount) {
        double price = currencyService.calcValueFor(symbol).get(symbol) * amount;
        return price;
    }


    protected void addCurrency(String username, Currency currency) {
        User user = loadUserByUsername(username);

        var updatedCurrencies = user.getCurrencies();

        updatedCurrencies.add(currency);
        user.setCurrencies(updatedCurrencies);
        repository.save(user);
        currencyRepository.saveAll(user.getCurrencies());
    }

    protected void removeCurrency(String username, List<Currency> currency) {
        User user = loadUserByUsername(username);

        var updatedCurrencies = user.getCurrencies();

        updatedCurrencies.removeAll(currency);
        user.setCurrencies(updatedCurrencies);
        repository.save(user);
        currencyRepository.saveAll(user.getCurrencies());
    }


    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }
}
