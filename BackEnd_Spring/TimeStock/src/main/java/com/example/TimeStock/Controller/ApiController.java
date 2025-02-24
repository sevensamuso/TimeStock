package com.example.TimeStock.Controller;

import com.example.TimeStock.service.StockPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Logger;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {

    private final StockPriceService stockPriceService;

    public ApiController(StockPriceService stockPriceService) {
        this.stockPriceService = stockPriceService;
    }

    /**
     * DB에 저장된 962개 종목에 대해 각각 API 호출 후 "stck_prpr" 값을 리스트로 반환합니다.
     */

    @GetMapping("/stock-price")
    public ResponseEntity<List<String>> getStockPrice() {
        List<String> prices = stockPriceService.fetchAllStockPrices();
        System.out.println(prices);
        return ResponseEntity.ok(prices);
    }
}