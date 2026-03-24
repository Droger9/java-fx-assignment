package com.example.fx.model.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class ErrorResponse {

    private final String errorCode;
    private final String message;
    private final LocalDateTime timestamp;
    private final Map<String, String> errors;

    public ErrorResponse(String errorCode, String message) {
        this(errorCode, message, null);
    }

    public ErrorResponse(String errorCode, String message, Map<String, String> errors) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.errors = errors;
    }
}
