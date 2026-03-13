package com.example.fx.service;

import com.example.fx.model.dto.ExchangeRateResponse;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateService {

    public ExchangeRateResponse getExchangeRate(String from, String to) {
        String normalizedFrom = from.toUpperCase();
        String normalizedTo = to.toUpperCase();

        validateCurrencyCode(normalizedFrom);
        validateCurrencyCode(normalizedTo);

        return new ExchangeRateResponse(normalizedFrom, normalizedTo, 0.92);
    }

    private void validateCurrencyCode(String currencyCode) {
        if (currencyCode == null || !currencyCode.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("Currency code must be exactly 3 letters");
        }
    }
}