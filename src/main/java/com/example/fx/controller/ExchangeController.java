package com.example.fx.controller;

import com.example.fx.model.dto.ConversionRequest;
import com.example.fx.model.dto.ConversionResponse;
import com.example.fx.model.dto.ExchangeRateResponse;
import com.example.fx.service.CurrencyConversionService;
import com.example.fx.service.ExchangeRateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExchangeController {

    private final ExchangeRateService exchangeRateService;
    private final CurrencyConversionService currencyConversionService;

    public ExchangeController(
            ExchangeRateService exchangeRateService,
            CurrencyConversionService currencyConversionService) {
        this.exchangeRateService = exchangeRateService;
        this.currencyConversionService = currencyConversionService;
    }

    @GetMapping("/rate")
    public ExchangeRateResponse getExchangeRate(@RequestParam String from, @RequestParam String to) {

        return exchangeRateService.getExchangeRate(from, to);
    }

    @PostMapping("/convert")
    public ConversionResponse convertCurrency(@RequestBody ConversionRequest request) {
        return currencyConversionService.convertCurrency(request);
    }
}