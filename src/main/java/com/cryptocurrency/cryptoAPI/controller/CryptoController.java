package com.cryptocurrency.cryptoAPI.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static com.cryptocurrency.cryptoAPI.util.ApiConstants.API_KEY;
import static com.cryptocurrency.cryptoAPI.util.ApiConstants.API_URL;

@RestController
@RequestMapping("/crypto")
public class CryptoController {

    @GetMapping
    public String test() {
        RestTemplate restTemplate = new RestTemplate();
        var res = restTemplate.getForObject(API_URL+"?api_key="+API_KEY, String.class);
        return res;
    }
}
