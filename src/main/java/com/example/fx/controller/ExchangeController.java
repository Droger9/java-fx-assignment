package com.example.fx.controller;

import com.example.fx.model.dto.ConversionHistoryResponse;
import com.example.fx.model.dto.ConversionRequest;
import com.example.fx.model.dto.ConversionResponse;
import com.example.fx.model.dto.ExchangeRateResponse;
import com.example.fx.service.CurrencyConversionService;
import com.example.fx.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Get exchange rate between two currencies")
    @GetMapping("/rate")
    public ExchangeRateResponse getExchangeRate(
            @Parameter(description = "Source currency code, for example USD")
            @RequestParam String from,

            @Parameter(description = "Target currency code, for example EUR")
            @RequestParam String to) {

        return exchangeRateService.getExchangeRate(from, to);
    }

    @Operation(summary = "Convert an amount from one currency to another")
    @PostMapping("/convert")
    public ConversionResponse convertCurrency(@Valid @RequestBody ConversionRequest request) {
        return currencyConversionService.convertCurrency(request);
    }

    @Operation(summary = "Get conversion history filtered by transaction ID or date")
    @GetMapping("/conversions")
    public Page<ConversionHistoryResponse> getConversionHistory(
            @Parameter(description = "Transaction UUID")
            @RequestParam(required = false) String transactionId,

            @Parameter(description = "Transaction date in format yyyy-MM-dd")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,

            @Parameter(description = "Page number, starting from 0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size) {

        return currencyConversionService.getConversionHistory(transactionId, date, page, size);
    }
}