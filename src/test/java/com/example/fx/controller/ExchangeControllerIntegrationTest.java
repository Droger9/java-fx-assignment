package com.example.fx.controller;

import com.example.fx.exception.ExternalServiceException;
import com.example.fx.model.dto.FixerResponse;
import com.example.fx.service.FixerClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "FIXER_API_KEY=test-key",
        "fixer.api.url=http://test-url"
})
@AutoConfigureMockMvc
class ExchangeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FixerClient fixerClient;

    @BeforeEach
    void setUp() {
        FixerResponse fixerResponse = new FixerResponse();
        fixerResponse.setSuccess(true);

        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 1.2);
        rates.put("EUR", 1.0);
        rates.put("BGN", 1.95);

        fixerResponse.setRates(rates);

        when(fixerClient.getLatestRates()).thenReturn(fixerResponse);
    }

    @Test
    void shouldReturnExchangeRate() throws Exception {
        mockMvc.perform(get("/rate")
                        .param("from", "USD")
                        .param("to", "EUR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.from").value("USD"))
                .andExpect(jsonPath("$.to").value("EUR"))
                .andExpect(jsonPath("$.rate").exists());
    }

    @Test
    void shouldConvertCurrency() throws Exception {
        String requestBody = """
                {
                  "amount": 100,
                  "from": "USD",
                  "to": "EUR"
                }
                """;

        mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").exists())
                .andExpect(jsonPath("$.from").value("USD"))
                .andExpect(jsonPath("$.to").value("EUR"))
                .andExpect(jsonPath("$.originalAmount").value(100))
                .andExpect(jsonPath("$.convertedAmount").exists())
                .andExpect(jsonPath("$.rate").exists());
    }

    @Test
    void shouldReturnValidationErrorForInvalidConversionRequest() throws Exception {
        String requestBody = """
                {
                  "amount": -5,
                  "from": "US",
                  "to": ""
                }
                """;

        mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.errors.amount").exists())
                .andExpect(jsonPath("$.errors.from").exists())
                .andExpect(jsonPath("$.errors.to").exists());
    }

    @Test
    void shouldReturnBadRequestForInvalidTransactionId() throws Exception {
        mockMvc.perform(get("/conversions")
                        .param("transactionId", "not-a-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnBadGatewayWhenExternalProviderFails() throws Exception {
        doThrow(new ExternalServiceException("Failed to fetch exchange rates from external provider"))
                .when(fixerClient).getLatestRates();

        mockMvc.perform(get("/rate")
                        .param("from", "USD")
                        .param("to", "EUR"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.errorCode").value("EXTERNAL_SERVICE_ERROR"))
                .andExpect(jsonPath("$.message").value("Failed to fetch exchange rates from external provider"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
