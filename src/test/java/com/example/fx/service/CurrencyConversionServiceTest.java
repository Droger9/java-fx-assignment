package com.example.fx.service;

import com.example.fx.model.dto.ConversionRequest;
import com.example.fx.model.dto.ConversionResponse;
import com.example.fx.model.dto.ExchangeRateResponse;
import com.example.fx.model.entity.ConversionTransaction;
import com.example.fx.repository.ConversionTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CurrencyConversionServiceTest {

    private ExchangeRateService exchangeRateService;
    private ConversionTransactionRepository repository;
    private CurrencyConversionService conversionService;

    @BeforeEach
    void setUp() {
        exchangeRateService = mock(ExchangeRateService.class);
        repository = mock(ConversionTransactionRepository.class);
        conversionService = new CurrencyConversionService(exchangeRateService, repository);
    }

    @Test
    void shouldConvertCurrencyAndSaveTransaction() {
        ConversionRequest request = new ConversionRequest();
        request.setAmount(new BigDecimal("100"));
        request.setFrom("USD");
        request.setTo("EUR");

        ExchangeRateResponse rateResponse =
                new ExchangeRateResponse("USD", "EUR", new BigDecimal("0.9"));

        when(exchangeRateService.getExchangeRate("USD", "EUR"))
                .thenReturn(rateResponse);

        ConversionTransaction saved = new ConversionTransaction();
        saved.setTransactionId(UUID.randomUUID());
        saved.setFromCurrency("USD");
        saved.setToCurrency("EUR");
        saved.setOriginalAmount(new BigDecimal("100"));
        saved.setConvertedAmount(new BigDecimal("90.00"));
        saved.setRate(new BigDecimal("0.9"));

        when(repository.save(any())).thenReturn(saved);

        ConversionResponse response = conversionService.convertCurrency(request);

        assertEquals("USD", response.getFrom());
        assertEquals("EUR", response.getTo());
        assertEquals(new BigDecimal("100"), response.getOriginalAmount());
        assertEquals(new BigDecimal("90.00"), response.getConvertedAmount());
        assertEquals(new BigDecimal("0.9"), response.getRate());

        verify(repository, times(1)).save(any());
    }
}