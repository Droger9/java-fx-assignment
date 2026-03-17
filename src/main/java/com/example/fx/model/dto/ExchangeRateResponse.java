package com.example.fx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ExchangeRateResponse {

    private String from;
    private String to;
    private BigDecimal rate;
}