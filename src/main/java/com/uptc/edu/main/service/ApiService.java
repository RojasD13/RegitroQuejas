package com.uptc.edu.main.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ApiService {

    private final WebClient webClient;

    @Value("auth.path")
    private String apiBaseUrl;

    public ApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String login(String email, String password) {
        String url = apiBaseUrl + "/login";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);
        return webClient.post()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String logout(String username) {
        String url = apiBaseUrl + "/logout";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", username);
        return webClient.post()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String isLogin(String email) {
        String url = apiBaseUrl + "/isLogin?email="+email;
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        return webClient.post()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
