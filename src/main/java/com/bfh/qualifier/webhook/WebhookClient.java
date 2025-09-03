package com.bfh.qualifier.webhook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class WebhookClient {
    private static final Logger log = LoggerFactory.getLogger(WebhookClient.class);
    private static final String GENERATE_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    private final RestTemplate restTemplate = new RestTemplate();

    public GenerateWebhookResponse generateWebhook(String name, String regNo, String email) {
        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("regNo", regNo);
        body.put("email", email);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        GenerateWebhookResponse response = restTemplate.postForObject(GENERATE_URL, entity, GenerateWebhookResponse.class);
        if (response == null || response.webhook() == null || response.accessToken() == null) {
            throw new IllegalStateException("Invalid response from generateWebhook");
        }
        log.info("Generated webhook: {}", response.webhook());
        return response;
    }

    public void postFinalQuery(String webhookUrl, String accessToken, String finalQuery) {
        Map<String, String> body = new HashMap<>();
        body.put("finalQuery", finalQuery);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", accessToken);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);
        restTemplate.postForLocation(webhookUrl, entity);
    }
}


