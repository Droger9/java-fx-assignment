package com.example.fx.service;

import com.example.fx.model.dto.ExchangeRateResponse;
import com.example.fx.model.dto.FixerResponse;
import com.example.fx.validation.CurrencyCodeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExchangeRateServiceTest {

    private FixerClient fixerClient;
    private CurrencyCodeValidator currencyCodeValidator;
    private ExchangeRateService exchangeRateService;

    @BeforeEach
    void setUp() {
        fixerClient = mock(FixerClient.class);
        currencyCodeValidator = new CurrencyCodeValidator();
        exchangeRateService = new ExchangeRateService(fixerClient, currencyCodeValidator);
    }

    @Test
    void shouldReturnRateOneWhenCurrenciesAreTheSame() {
        ExchangeRateResponse response = exchangeRateService.getExchangeRate("usd", "usd");

        assertEquals("USD", response.getFrom());
        assertEquals("USD", response.getTo());
        assertEquals(BigDecimal.ONE, response.getRate());
    }

    @Test
    void shouldThrowExceptionForInvalidCurrencyCode() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> exchangeRateService.getExchangeRate("US", "EUR")
        );

        assertEquals("Currency code must be exactly 3 letters", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForUnsupportedCurrencyCode() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> exchangeRateService.getExchangeRate("ABC", "EUR")
        );

        assertEquals("Invalid currency code: ABC", exception.getMessage());
    }

    @Test
    void shouldCalculateExchangeRateFromFixerRates() {
        FixerResponse fixerResponse = new FixerResponse();
        fixerResponse.setSuccess(true);

        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 1.2);
        rates.put("BGN", 1.95);
        fixerResponse.setRates(rates);

        when(fixerClient.getLatestRates()).thenReturn(fixerResponse);

        ExchangeRateResponse response = exchangeRateService.getExchangeRate("USD", "BGN");

        assertEquals("USD", response.getFrom());
        assertEquals("BGN", response.getTo());
        assertEquals(new BigDecimal("1.625000"), response.getRate());
    }
}
