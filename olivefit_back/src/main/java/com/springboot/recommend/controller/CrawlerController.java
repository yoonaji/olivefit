package com.springboot.recommend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/crawler")
public class CrawlerController {

    private final RestTemplate restTemplate = new RestTemplate();

    // Cloud Run 서비스 URL들
    private static final String PRODUCTS_CRAWLER_URL = "https://products-crawler-649511210818.us-central1.run.app/run-crawler";
    private static final String REVIEWS_CRAWLER_URL = "https://reviews-crawler-649511210818.us-central1.run.app/run-crawler";

    @PostMapping("/run")
    public ResponseEntity<String> runCrawler(@RequestParam String target) {
        String crawlerUrl;

        // target 파라미터 값으로 URL 결정
        switch (target.toLowerCase()) {
            case "product":
                crawlerUrl = PRODUCTS_CRAWLER_URL;
                break;
            case "review":
                crawlerUrl = REVIEWS_CRAWLER_URL;
                break;
            default:
                return ResponseEntity.badRequest().body("Invalid target. Use 'product' or 'review'.");
        }

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(crawlerUrl, null, String.class);
            return ResponseEntity.ok("크롤링 요청 성공: " + response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("크롤링 요청 실패: " + e.getMessage());
        }
    }
}
