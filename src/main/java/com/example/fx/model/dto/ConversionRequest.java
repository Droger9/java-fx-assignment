package com.example.fx.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConversionRequest {

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private Double amount;

    @NotBlank(message = "Source currency is required")
    @Pattern(regexp = "^[A-Za-z]{3}$", message = "Source currency must be exactly 3 letters")
    private String from;

    @NotBlank(message = "Target currency is required")
    @Pattern(regexp = "^[A-Za-z]{3}$", message = "Target currency must be exactly 3 letters")
    private String to;
}