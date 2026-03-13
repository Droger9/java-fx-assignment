package com.example.fx.service;

import com.example.fx.config.FixerConfig;
import com.example.fx.model.dto.FixerResponse;
import org.springframework.stereotype.Service;
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
        return restTemplate.getForObject(url, FixerResponse.class);
    }
}