package com.example.fx.service;

import com.example.fx.model.dto.ConversionHistoryResponse;
import com.example.fx.model.dto.ConversionRequest;
import com.example.fx.model.dto.ConversionResponse;
import com.example.fx.model.dto.ExchangeRateResponse;
import com.example.fx.model.entity.ConversionTransaction;
import com.example.fx.repository.ConversionTransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CurrencyConversionService {

    private final ExchangeRateService exchangeRateService;
    private final ConversionTransactionRepository conversionTransactionRepository;

    public CurrencyConversionService(
            ExchangeRateService exchangeRateService,
            ConversionTransactionRepository conversionTransactionRepository) {
        this.exchangeRateService = exchangeRateService;
        this.conversionTransactionRepository = conversionTransactionRepository;
    }

    public ConversionResponse convertCurrency(ConversionRequest request) {

        validateAmount(request.getAmount());

        ExchangeRateResponse rateResponse =
                exchangeRateService.getExchangeRate(request.getFrom(), request.getTo());

        double convertedAmount = request.getAmount() * rateResponse.getRate();

        ConversionTransaction transaction = new ConversionTransaction();
        transaction.setFromCurrency(rateResponse.getFrom());
        transaction.setToCurrency(rateResponse.getTo());
        transaction.setOriginalAmount(request.getAmount());
        transaction.setConvertedAmount(convertedAmount);
        transaction.setRate(rateResponse.getRate());
        transaction.setTransactionDate(LocalDateTime.now());

        ConversionTransaction savedTransaction = conversionTransactionRepository.save(transaction);

        return new ConversionResponse(
                savedTransaction.getTransactionId().toString(),
                savedTransaction.getFromCurrency(),
                savedTransaction.getToCurrency(),
                savedTransaction.getOriginalAmount(),
                savedTransaction.getConvertedAmount(),
                savedTransaction.getRate()
        );
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
    }

    public Page<ConversionHistoryResponse> getConversionHistory(String transactionId, LocalDate date, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ConversionTransaction> transactions;

        if (transactionId != null) {
            UUID id = UUID.fromString(transactionId);
            transactions = conversionTransactionRepository.findByTransactionId(id, pageable);
        } else if (date != null) {

            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);

            transactions = conversionTransactionRepository
                    .findByTransactionDateBetween(startOfDay, endOfDay, pageable);

        } else {
            throw new IllegalArgumentException("Either transactionId or date must be provided");
        }

        return transactions.map(t -> new ConversionHistoryResponse(
                t.getTransactionId().toString(),
                t.getFromCurrency(),
                t.getToCurrency(),
                t.getOriginalAmount(),
                t.getConvertedAmount(),
                t.getRate(),
                t.getTransactionDate()
        ));
    }
}