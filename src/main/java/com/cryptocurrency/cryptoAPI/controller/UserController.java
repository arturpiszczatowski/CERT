package com.cryptocurrency.cryptoAPI.controller;

import com.cryptocurrency.cryptoAPI.model.User;
import com.cryptocurrency.cryptoAPI.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.function.Function;

@RestController
@RequestMapping("/user")
public class UserController extends CrudController<User> {
    UserService userService;

    protected UserController(UserService service) {
        super(service);
        this.userService = service;
    }


//    @PostMapping("/buy/{symbol}/{amount}")
//    public ResponseEntity buyCurrency(@PathVariable String symbol, @PathVariable int amount) {
//
//    }

    @Override
    public Function<User, Map<String, Object>> transformToDTO() {
        return null;
    }
}
