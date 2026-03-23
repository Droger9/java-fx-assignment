package com.example.fx.service;

import com.example.fx.mapper.ConversionTransactionMapper;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CurrencyConversionService {

    private final ExchangeRateService exchangeRateService;
    private final ConversionTransactionRepository conversionTransactionRepository;
    private final ConversionTransactionMapper conversionTransactionMapper;

    public CurrencyConversionService(
            ExchangeRateService exchangeRateService,
            ConversionTransactionRepository conversionTransactionRepository,
            ConversionTransactionMapper conversionTransactionMapper) {
        this.exchangeRateService = exchangeRateService;
        this.conversionTransactionRepository = conversionTransactionRepository;
        this.conversionTransactionMapper = conversionTransactionMapper;
    }

    public ConversionResponse convertCurrency(ConversionRequest request) {
        ExchangeRateResponse rateResponse =
                exchangeRateService.getExchangeRate(request.getFrom(), request.getTo());

        BigDecimal convertedAmount = request.getAmount()
                .multiply(rateResponse.getRate())
                .setScale(2, RoundingMode.HALF_UP);

        ConversionTransaction transaction = conversionTransactionMapper.toEntity(
                request.getAmount(),
                convertedAmount,
                rateResponse,
                LocalDateTime.now()
        );

        ConversionTransaction savedTransaction = conversionTransactionRepository.save(transaction);

        return conversionTransactionMapper.toConversionResponse(savedTransaction);
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

        return transactions.map(conversionTransactionMapper::toConversionHistoryResponse);
    }
}
