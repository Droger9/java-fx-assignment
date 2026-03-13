package com.example.fx.controller;

import com.example.fx.model.dto.ConversionHistoryResponse;
import com.example.fx.model.dto.ConversionRequest;
import com.example.fx.model.dto.ConversionResponse;
import com.example.fx.model.dto.ExchangeRateResponse;
import com.example.fx.service.CurrencyConversionService;
import com.example.fx.service.ExchangeRateService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

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

    @GetMapping("/conversions")
    public Page<ConversionHistoryResponse> getConversionHistory(
            @RequestParam(required = false) String transactionId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return currencyConversionService.getConversionHistory(transactionId, date, page, size);
    }
}