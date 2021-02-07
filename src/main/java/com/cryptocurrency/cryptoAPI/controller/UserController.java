package com.cryptocurrency.cryptoAPI.controller;

import com.cryptocurrency.cryptoAPI.model.User;
import com.cryptocurrency.cryptoAPI.repository.UserRepository;
import com.cryptocurrency.cryptoAPI.security.TokenAuthenticationFilter;
import com.cryptocurrency.cryptoAPI.service.CurrencyService;
import com.cryptocurrency.cryptoAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController extends CrudController<User> {
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CurrencyService currencyService;

    protected UserController(UserService service) {
        super(service);
        this.userService = service;
    }

    @PostMapping("/buy/{symbol}/{amount}")
    public ResponseEntity buyCurrecy(@PathVariable String symbol, @PathVariable int amount) {
        userService.buy(currentUserName(), symbol, amount);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/sell/{symbol}/{amount}")
    public ResponseEntity sellCurrency(@PathVariable String symbol, @PathVariable int amount) {
        userService.sell(currentUserName(), symbol, amount);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/view")
    public ResponseEntity viewGoodies() {
        var map = currencyService.findByUsername(currentUserName());
        return new ResponseEntity(map, HttpStatus.OK);
    }

    @Override
    public Function<User, Map<String, Object>> transformToDTO() {
        return user -> {
            var payload = new LinkedHashMap<String, Object>();
            payload.put("id", user.getId());
            payload.put("username", user.getUsername());
            payload.put("money", user.getMoney());
            payload.put("Currencies", user.getCurrencies().stream()
                    .map(currency -> currency.getSymbol())
                    .collect(Collectors.toList()));
            return payload;
        };
    }

    public String currentUserName(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        return name;
    }
}
