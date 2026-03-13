package com.example.fx.controller;

import com.example.fx.model.dto.ExchangeRateResponse;
import com.example.fx.service.ExchangeRateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExchangeController {

    private final ExchangeRateService exchangeRateService;

    public ExchangeController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/rate")
    public ExchangeRateResponse getExchangeRate(@RequestParam String from, @RequestParam String to) {

        return exchangeRateService.getExchangeRate(from, to);
    }
}