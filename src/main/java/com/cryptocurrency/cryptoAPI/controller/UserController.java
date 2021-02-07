package com.cryptocurrency.cryptoAPI.controller;

import com.cryptocurrency.cryptoAPI.model.User;
import com.cryptocurrency.cryptoAPI.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController extends CrudController<User> {
    UserService userService;

    protected UserController(UserService service) {
        super(service);
        this.userService = service;
    }

    @PostMapping("/buy/{symbol}/{amount}")
    public ResponseEntity buyCurrency(@PathVariable String symbol, @PathVariable int amount) {
        userService.buy("BrodoFagins", symbol, amount);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/sell/{symbol}/{amount}")
    public ResponseEntity sellCurrency(@PathVariable String symbol, @PathVariable int amount) {
        userService.sell("BrodoFagins", symbol, amount);
        return new ResponseEntity(HttpStatus.OK);
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
}
