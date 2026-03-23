package com.example.fx.mapper;

import com.example.fx.model.dto.ConversionHistoryResponse;
import com.example.fx.model.dto.ConversionResponse;
import com.example.fx.model.dto.ExchangeRateResponse;
import com.example.fx.model.entity.ConversionTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConversionTransactionMapperTest {

    private ConversionTransactionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ConversionTransactionMapper();
    }

    @Test
    void shouldMapToEntity() {
        ExchangeRateResponse rateResponse =
                new ExchangeRateResponse("USD", "EUR", new BigDecimal("0.90"));
        LocalDateTime transactionDate = LocalDateTime.of(2026, 3, 23, 10, 30);

        ConversionTransaction transaction = mapper.toEntity(
                new BigDecimal("100.00"),
                new BigDecimal("90.00"),
                rateResponse,
                transactionDate
        );

        assertEquals("USD", transaction.getFromCurrency());
        assertEquals("EUR", transaction.getToCurrency());
        assertEquals(new BigDecimal("100.00"), transaction.getOriginalAmount());
        assertEquals(new BigDecimal("90.00"), transaction.getConvertedAmount());
        assertEquals(new BigDecimal("0.90"), transaction.getRate());
        assertEquals(transactionDate, transaction.getTransactionDate());
    }

    @Test
    void shouldMapToConversionResponse() {
        ConversionTransaction transaction = createTransaction();

        ConversionResponse response = mapper.toConversionResponse(transaction);

        assertEquals(transaction.getTransactionId().toString(), response.getTransactionId());
        assertEquals(transaction.getFromCurrency(), response.getFrom());
        assertEquals(transaction.getToCurrency(), response.getTo());
        assertEquals(transaction.getOriginalAmount(), response.getOriginalAmount());
        assertEquals(transaction.getConvertedAmount(), response.getConvertedAmount());
        assertEquals(transaction.getRate(), response.getRate());
    }

    @Test
    void shouldMapToConversionHistoryResponse() {
        ConversionTransaction transaction = createTransaction();

        ConversionHistoryResponse response = mapper.toConversionHistoryResponse(transaction);

        assertEquals(transaction.getTransactionId().toString(), response.getTransactionId());
        assertEquals(transaction.getFromCurrency(), response.getFromCurrency());
        assertEquals(transaction.getToCurrency(), response.getToCurrency());
        assertEquals(transaction.getOriginalAmount(), response.getOriginalAmount());
        assertEquals(transaction.getConvertedAmount(), response.getConvertedAmount());
        assertEquals(transaction.getRate(), response.getRate());
        assertEquals(transaction.getTransactionDate(), response.getTransactionDate());
    }

    private ConversionTransaction createTransaction() {
        ConversionTransaction transaction = new ConversionTransaction();
        transaction.setTransactionId(UUID.randomUUID());
        transaction.setFromCurrency("USD");
        transaction.setToCurrency("EUR");
        transaction.setOriginalAmount(new BigDecimal("100.00"));
        transaction.setConvertedAmount(new BigDecimal("90.00"));
        transaction.setRate(new BigDecimal("0.90"));
        transaction.setTransactionDate(LocalDateTime.of(2026, 3, 23, 10, 30));
        return transaction;
    }
}
