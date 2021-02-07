package com.cryptocurrency.cryptoAPI.controller;

import com.cryptocurrency.cryptoAPI.model.User;
import com.cryptocurrency.cryptoAPI.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@RestController
@RequestMapping("/admin")
public class AdminController extends CrudController<User> {
    UserService userService;

    protected AdminController(UserService service) {
        super(service);
        this.userService = service;
    }

    @PostMapping("/users/{id}/authorities/{authority}")
    public ResponseEntity addAuthority(@PathVariable Long id, @PathVariable String authority) {
        User user = userService.getById(id);
        user.addAuthority(() -> authority);
        return createOrUpdate(user);
    }

    @PutMapping("/users/{id}/authorities/{authority}")
    public ResponseEntity setAuthority(@PathVariable Long id, @PathVariable String authority) {
        User user = userService.getById(id);
        user.setAuthority(authority);
        return createOrUpdate(user);
    }

    @DeleteMapping("/users/{id}/authorities/{authority}")
    public ResponseEntity deleteAuthority(@PathVariable Long id, @PathVariable String authority) {
        User user = userService.getById(id);
        user.removeAuthority(() -> authority);
        return createOrUpdate(user);
    }

    @Override
    public Function<User, Map<String, Object>> transformToDTO() {
        return user -> {
            var payload = new LinkedHashMap<String, Object>();
            payload.put("id", user.getId());
            payload.put("username", user.getUsername());
            payload.put("authorities", user.getAuthorities().stream().map(GrantedAuthority::getAuthority));

            return payload;
        };
    }

    public ResponseEntity createOrUpdate(User user) {
        try {
            userService.createOrUpdate(user);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}