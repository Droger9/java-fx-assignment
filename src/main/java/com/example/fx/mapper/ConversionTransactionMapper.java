package com.example.fx.mapper;

import com.example.fx.model.dto.ConversionHistoryResponse;
import com.example.fx.model.dto.ConversionResponse;
import com.example.fx.model.dto.ExchangeRateResponse;
import com.example.fx.model.entity.ConversionTransaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class ConversionTransactionMapper {

    public ConversionTransaction toEntity(
            BigDecimal originalAmount,
            BigDecimal convertedAmount,
            ExchangeRateResponse rateResponse,
            LocalDateTime transactionDate) {

        ConversionTransaction transaction = new ConversionTransaction();
        transaction.setFromCurrency(rateResponse.getFrom());
        transaction.setToCurrency(rateResponse.getTo());
        transaction.setOriginalAmount(originalAmount);
        transaction.setConvertedAmount(convertedAmount);
        transaction.setRate(rateResponse.getRate());
        transaction.setTransactionDate(transactionDate);

        return transaction;
    }

    public ConversionResponse toConversionResponse(ConversionTransaction transaction) {
        return new ConversionResponse(
                transaction.getTransactionId().toString(),
                transaction.getFromCurrency(),
                transaction.getToCurrency(),
                transaction.getOriginalAmount(),
                transaction.getConvertedAmount(),
                transaction.getRate()
        );
    }

    public ConversionHistoryResponse toConversionHistoryResponse(ConversionTransaction transaction) {
        return new ConversionHistoryResponse(
                transaction.getTransactionId().toString(),
                transaction.getFromCurrency(),
                transaction.getToCurrency(),
                transaction.getOriginalAmount(),
                transaction.getConvertedAmount(),
                transaction.getRate(),
                transaction.getTransactionDate()
        );
    }
}
