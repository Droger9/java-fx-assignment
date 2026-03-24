package com.example.fx.validation;

import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CurrencyCodeValidator {

    private final Set<String> supportedCurrencyCodes = Currency.getAvailableCurrencies().stream()
            .map(Currency::getCurrencyCode)
            .collect(Collectors.toSet());

    public String normalizeAndValidate(String currencyCode) {
        if (currencyCode == null) {
            throw new IllegalArgumentException("Currency code is required");
        }

        String normalizedCurrencyCode = currencyCode.toUpperCase();

        if (!normalizedCurrencyCode.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("Currency code must be exactly 3 letters");
        }

        if (!supportedCurrencyCodes.contains(normalizedCurrencyCode)) {
            throw new IllegalArgumentException("Invalid currency code: " + normalizedCurrencyCode);
        }

        return normalizedCurrencyCode;
    }
}
