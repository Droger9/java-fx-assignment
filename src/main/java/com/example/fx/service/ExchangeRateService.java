package com.example.fx.service;

import com.example.fx.model.dto.ExchangeRateResponse;
import com.example.fx.model.dto.FixerResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ExchangeRateService {

    private final FixerClient fixerClient;

    public ExchangeRateService(FixerClient fixerClient) {
        this.fixerClient = fixerClient;
    }

    public ExchangeRateResponse getExchangeRate(String from, String to) {

        String normalizedFrom = from.toUpperCase();
        String normalizedTo = to.toUpperCase();

        validateCurrencyCode(normalizedFrom);
        validateCurrencyCode(normalizedTo);

        if (normalizedFrom.equals(normalizedTo)) {
            return new ExchangeRateResponse(normalizedFrom, normalizedTo, 1.0);
        }

        FixerResponse fixerResponse = fixerClient.getLatestRates();

        if (!fixerResponse.isSuccess()) {
            throw new IllegalArgumentException("Failed to fetch exchange rates from external provider");
        }

        Map<String, Double> rates = fixerResponse.getRates();

        Double fromRate = getRateForCurrency(normalizedFrom, rates);
        Double toRate = getRateForCurrency(normalizedTo, rates);

        double rate = toRate / fromRate;

        return new ExchangeRateResponse(normalizedFrom, normalizedTo, rate);
    }

    private void validateCurrencyCode(String currencyCode) {
        if (currencyCode == null || !currencyCode.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("Currency code must be exactly 3 letters");
        }
    }

    private Double getRateForCurrency(String currencyCode, Map<String, Double> rates) {
        if ("EUR".equals(currencyCode)) {
            return 1.0;
        }

        Double rate = rates.get(currencyCode);

        if (rate == null) {
            throw new IllegalArgumentException("Unsupported currency code: " + currencyCode);
        }

        return rate;
    }
}