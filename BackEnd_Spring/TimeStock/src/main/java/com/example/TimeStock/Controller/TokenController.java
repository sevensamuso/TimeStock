package com.example.TimeStock.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
@RestController
public class TokenController {

    @Value("${kis.token.url}")
    private String tokenUrl;

    @Value("${kis.api.app_key}")
    private String appKey;

    @Value("${kis.api.app_secret}")
    private String appSecret;

    /**
     * 접근토큰 발급 API를 호출하는 엔드포인트.
     * POST /getToken 으로 호출하면 한국투자증권 API로 요청을 보내고,
     * 그 결과(access_token, token_type, expires_in 등)를 응답으로 반환합니다.
     */

    @PostMapping("/oauth2/tokenP")
    public ResponseEntity<String> getToken() {
        RestTemplate restTemplate = new RestTemplate();

        // 요청 본문 구성
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("grant_type", "client_credentials");
        requestBody.put("appkey", appKey);
        requestBody.put("appsecret", appSecret);

        // 요청 헤더 구성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        // POST 요청으로 토큰 발급 API 호출
        ResponseEntity<String> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}
