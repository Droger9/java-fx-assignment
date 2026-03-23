package com.example.fx.service;

import com.example.fx.mapper.ConversionTransactionMapper;
import com.example.fx.model.dto.ConversionRequest;
import com.example.fx.model.dto.ConversionResponse;
import com.example.fx.model.dto.ExchangeRateResponse;
import com.example.fx.model.entity.ConversionTransaction;
import com.example.fx.repository.ConversionTransactionRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CurrencyConversionServiceTest {

    private ExchangeRateService exchangeRateService;
    private ConversionTransactionRepository repository;
    private ConversionTransactionMapper mapper;
    private CurrencyConversionService conversionService;

    @BeforeEach
    void setUp() {
        exchangeRateService = mock(ExchangeRateService.class);
        repository = mock(ConversionTransactionRepository.class);
        mapper = new ConversionTransactionMapper();
        conversionService = new CurrencyConversionService(exchangeRateService, repository, mapper);
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

    @Test
    void shouldReturnConversionHistoryWhenFilteringByTransactionId() {
        ConversionTransaction transaction = createTransaction();

        when(repository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(transaction)));

        var response = conversionService.getConversionHistory(
                transaction.getTransactionId().toString(),
                null,
                0,
                10
        );

        assertEquals(1, response.getTotalElements());
        assertEquals(transaction.getTransactionId().toString(), response.getContent().get(0).getTransactionId());

        verify(repository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void shouldReturnConversionHistoryWhenFilteringByDate() {
        ConversionTransaction transaction = createTransaction();

        when(repository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(transaction)));

        var response = conversionService.getConversionHistory(
                null,
                LocalDate.of(2026, 3, 23),
                0,
                10
        );

        assertEquals(1, response.getTotalElements());
        assertEquals(transaction.getTransactionDate(), response.getContent().get(0).getTransactionDate());

        verify(repository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void shouldThrowExceptionWhenNoHistoryFilterIsProvided() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> conversionService.getConversionHistory(null, null, 0, 10)
        );

        assertEquals("Either transactionId or date must be provided", exception.getMessage());
        verify(repository, never()).findAll(any(Specification.class), any(Pageable.class));
    }

    private ConversionTransaction createTransaction() {
        ConversionTransaction transaction = new ConversionTransaction();
        transaction.setTransactionId(UUID.randomUUID());
        transaction.setFromCurrency("USD");
        transaction.setToCurrency("EUR");
        transaction.setOriginalAmount(new BigDecimal("100"));
        transaction.setConvertedAmount(new BigDecimal("90.00"));
        transaction.setRate(new BigDecimal("0.9"));
        transaction.setTransactionDate(LocalDateTime.of(2026, 3, 23, 10, 30));
        return transaction;
    }
}
