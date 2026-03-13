package com.example.fx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExchangeRateResponse {

    private String from;
    private String to;
    private double rate;

}
