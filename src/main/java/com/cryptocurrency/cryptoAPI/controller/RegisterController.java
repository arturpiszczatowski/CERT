package com.cryptocurrency.cryptoAPI.controller;

import com.cryptocurrency.cryptoAPI.model.User;
import com.cryptocurrency.cryptoAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class RegisterController {

    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity register(@RequestBody User user) {
        try {
            if(userService.findUserByUsername(user.getUsername()) == null){
                userService.createOrUpdate(user);
                return new ResponseEntity(HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.EXPECTATION_FAILED);
        }
    }
}
