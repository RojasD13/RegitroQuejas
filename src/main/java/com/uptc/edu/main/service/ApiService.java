package com.uptc.edu.main.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class ApiService {

    private final WebClient webClient;

    @Value("${AUTH_PATH}")
    private String apiBaseUrl;
    public static final String AUTH_SERVICE_UNAVAILABLE = "El servicio de autenticación no está disponible. Intente más tarde.";
    public static final String INVALID_CREDENTIALS = "Correo o contraseña incorrectos.";
    public static final String LOGIN_SUCCESS = "Inicio de sesión exitoso. ¡Bienvenido!";
    public static final String LOGOUT_SUCCESS = "Sesión cerrada correctamente";
    public static final String SESSION_EXPIRED = "Usuario no autorizado o sesión expirada";
    public static final String LOGIN_REQUIRED = "Tiene que iniciar sesión para acceder a esta página";
    public static class AuthServiceException extends RuntimeException {
        public AuthServiceException(String message) {
            super(message);
        }
    }

    public ApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String login(String email, String password) {
        try {
            String url = apiBaseUrl + "/login";
            var requestBody = java.util.Map.of("email", email, "password", password);
            
            return webClient.post()
                    .uri(url)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (WebClientRequestException e) {
            throw new AuthServiceException(AUTH_SERVICE_UNAVAILABLE);
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().is4xxClientError()) {
                return "error";
            } else {
                throw new AuthServiceException(AUTH_SERVICE_UNAVAILABLE);
            }
        }
    }
    public String logout(String username) {
        String url = apiBaseUrl + "/logout";
        var requestBody = java.util.Map.of("username", username);
        return webClient.post()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    public String isLogin(String email) {
        String url = apiBaseUrl + "/isLogin?email=" + email;
        var requestBody = java.util.Map.of("email", email);
        return webClient.post()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}