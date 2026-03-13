package com.example.fx.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConversionRequest {

    private double amount;
    private String from;
    private String to;
}