package com.example.fx.service;

import com.example.fx.exception.ExternalServiceException;
import com.example.fx.model.dto.ExchangeRateResponse;
import com.example.fx.model.dto.FixerResponse;
import com.example.fx.validation.CurrencyCodeValidator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class ExchangeRateService {

    private final FixerClient fixerClient;
    private final CurrencyCodeValidator currencyCodeValidator;

    public ExchangeRateService(FixerClient fixerClient, CurrencyCodeValidator currencyCodeValidator) {
        this.fixerClient = fixerClient;
        this.currencyCodeValidator = currencyCodeValidator;
    }

    public ExchangeRateResponse getExchangeRate(String from, String to) {
        String normalizedFrom = currencyCodeValidator.normalizeAndValidate(from);
        String normalizedTo = currencyCodeValidator.normalizeAndValidate(to);

        if (normalizedFrom.equals(normalizedTo)) {
            return new ExchangeRateResponse(normalizedFrom, normalizedTo, BigDecimal.ONE);
        }

        FixerResponse fixerResponse = fixerClient.getLatestRates();

        if (!fixerResponse.isSuccess()) {
            throw new ExternalServiceException("Failed to fetch exchange rates from external provider");
        }

        Map<String, Double> rates = fixerResponse.getRates();

        BigDecimal fromRate = getRateForCurrency(normalizedFrom, rates);
        BigDecimal toRate = getRateForCurrency(normalizedTo, rates);

        BigDecimal rate = toRate.divide(fromRate, 6, RoundingMode.HALF_UP);

        return new ExchangeRateResponse(normalizedFrom, normalizedTo, rate);
    }

    private BigDecimal getRateForCurrency(String currencyCode, Map<String, Double> rates) {
        if ("EUR".equals(currencyCode)) {
            return BigDecimal.ONE;
        }

        Double rate = rates.get(currencyCode);

        if (rate == null) {
            throw new IllegalArgumentException("Unsupported currency code: " + currencyCode);
        }

        return BigDecimal.valueOf(rate);
    }
}
