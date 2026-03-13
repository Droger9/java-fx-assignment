package com.example.fx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConversionResponse {

    private String transactionId;
    private String from;
    private String to;
    private double originalAmount;
    private double convertedAmount;
    private double rate;
}