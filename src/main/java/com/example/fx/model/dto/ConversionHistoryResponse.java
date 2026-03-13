package com.example.fx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ConversionHistoryResponse {

    private String transactionId;
    private String fromCurrency;
    private String toCurrency;
    private double originalAmount;
    private double convertedAmount;
    private double rate;
    private LocalDateTime transactionDate;
}