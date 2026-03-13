package com.example.fx.service;

import com.example.fx.model.dto.ConversionRequest;
import com.example.fx.model.dto.ConversionResponse;
import com.example.fx.model.dto.ExchangeRateResponse;
import com.example.fx.model.entity.ConversionTransaction;
import com.example.fx.repository.ConversionTransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
}