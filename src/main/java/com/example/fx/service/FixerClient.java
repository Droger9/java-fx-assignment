package com.example.fx.service;

import com.example.fx.config.FixerConfig;
import com.example.fx.exception.ExternalServiceException;
import com.example.fx.model.dto.FixerResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class FixerClient {

    private final FixerConfig fixerConfig;
    private final RestTemplate restTemplate;

    public FixerClient(FixerConfig fixerConfig) {
        this.fixerConfig = fixerConfig;
        this.restTemplate = new RestTemplate();
    }

    public FixerResponse getLatestRates() {
        String url = fixerConfig.getApiUrl() + "/latest?access_key=" + fixerConfig.getApiKey();

        try {
            FixerResponse response = restTemplate.getForObject(url, FixerResponse.class);

            if (response == null) {
                throw new ExternalServiceException("Exchange rate provider returned an empty response");
            }

            return response;
        } catch (RestClientException ex) {
            throw new ExternalServiceException("Failed to fetch exchange rates from external provider");
        }
    }
}
