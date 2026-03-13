package com.example.fx.service;

import com.example.fx.model.dto.ConversionRequest;
import com.example.fx.model.dto.ConversionResponse;
import com.example.fx.model.dto.ExchangeRateResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CurrencyConversionService {

    private final ExchangeRateService exchangeRateService;

    public CurrencyConversionService(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    public ConversionResponse convertCurrency(ConversionRequest request) {

        validateAmount(request.getAmount());

        ExchangeRateResponse rateResponse =
                exchangeRateService.getExchangeRate(request.getFrom(), request.getTo());

        double convertedAmount = request.getAmount() * rateResponse.getRate();
        String transactionId = UUID.randomUUID().toString();

        return new ConversionResponse(
                transactionId,
                rateResponse.getFrom(),
                rateResponse.getTo(),
                request.getAmount(),
                convertedAmount,
                rateResponse.getRate()
        );
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
    }
}