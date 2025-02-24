package com.example.TimeStock.service;

import com.example.TimeStock.domain.KISStock;
import com.example.TimeStock.repasitory.KISStockRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class StockPriceService {

    private final KISStockRepository kisStockRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public StockPriceService(KISStockRepository kisStockRepository) {
        this.kisStockRepository = kisStockRepository;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }
    @Value("${kis.api.url}")
    private String apiUrl;

    @Value("${kis.oauth.token}")
    private String accessToken;

    @Value("${kis.api.app_key}")
    private String appKey;

    @Value("${kis.api.app_secret}")
    private String appSecret;

    public List<String> fetchAllStockPrices() {
        RateLimiter rateLimiter = RateLimiter.create(20);
        long startTime = System.currentTimeMillis();

        List<KISStock> stockList = kisStockRepository.findAll();
        List<String> stockPrices = new ArrayList<>();

        for (KISStock stock : stockList) {
            rateLimiter.acquire();
            String stockCode = stock.getCode();
            String urlWithParams = apiUrl + "?FID_COND_MRKT_DIV_CODE=J&FID_INPUT_ISCD=" + stockCode;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("authorization", "Bearer " + accessToken);
            headers.set("appkey", appKey);
            headers.set("appsecret", appSecret);
            headers.set("tr_id", "FHKST01010100");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        urlWithParams,
                        HttpMethod.GET,
                        entity,
                        String.class
                );

                JsonNode rootNode = objectMapper.readTree(response.getBody());
                String price = rootNode.path("output").path("stck_prpr").asText();
                stockPrices.add(price);
                log.info("Fetched price for {}: {}", stockCode, price);

            } catch (Exception e) {

                log.error("Error fetching price for code {}: {}", stockCode, e.getMessage());
                stockPrices.add(null);
            }
        }
        long endTime = System.currentTimeMillis();
        long durationInMillis = endTime - startTime;
        System.out.println("실행 시간 (밀리초): " + durationInMillis);

        return stockPrices;
    }
}