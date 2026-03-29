package com.example.fx.service;

import com.example.fx.config.FixerConfig;
import com.example.fx.exception.ExternalServiceException;
import com.example.fx.model.dto.FixerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class FixerClient {

    private static final Logger logger = LoggerFactory.getLogger(FixerClient.class);

    private final FixerConfig fixerConfig;
    private final RestTemplate restTemplate;

    public FixerClient(FixerConfig fixerConfig) {
        this.fixerConfig = fixerConfig;
        this.restTemplate = new RestTemplate();
    }

    @Cacheable("latestRates")
    public FixerResponse getLatestRates() {
        logger.info("Fetching latest exchange rates");
        return fetchLatestRatesFromProvider();
    }

    @CachePut("latestRates")
    @Scheduled(
            fixedRateString = "${fixer.cache.refresh-rate-ms:3600000}",
            initialDelayString = "${fixer.cache.refresh-rate-ms:3600000}"
    )
    public FixerResponse refreshLatestRatesCache() {
        logger.info("Refreshing cached exchange rates");
        return fetchLatestRatesFromProvider();
    }

    private FixerResponse fetchLatestRatesFromProvider() {
        String url = fixerConfig.getApiUrl() + "/latest?access_key=" + fixerConfig.getApiKey();

        try {
            FixerResponse response = restTemplate.getForObject(url, FixerResponse.class);

            if (response == null) {
                logger.error("Exchange rate provider returned an empty response");
                throw new ExternalServiceException("Exchange rate provider returned an empty response");
            }

            logger.info("Successfully fetched exchange rates from external provider");
            return response;
        } catch (RestClientException ex) {
            logger.error("Failed to fetch exchange rates from external provider", ex);
            throw new ExternalServiceException("Failed to fetch exchange rates from external provider");
        }
    }
}
