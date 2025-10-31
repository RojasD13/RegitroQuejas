package com.uptc.edu.main.config;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.uptc.edu.main.service.ApiService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse; 
 
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final ApiService apiService;

    public AuthInterceptor(ApiService apiService) {
        this.apiService = apiService;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String email = (String) request.getSession().getAttribute("userEmail");

        if (email == null || email.isBlank()) {
            handleUnauthorizedRequest(response, ApiService.LOGIN_REQUIRED);
            return false;
        }
        try {
            if (!Boolean.parseBoolean(apiService.isLogin(email))) {
                handleUnauthorizedRequest(response, ApiService.SESSION_EXPIRED);
                return false;
            }
        } catch (Exception e) {
            handleUnauthorizedRequest(response, ApiService.AUTH_SERVICE_UNAVAILABLE);
            return false;
        }
        return true;
    }
    private void handleUnauthorizedRequest(HttpServletResponse response, String message) throws IOException {
        response.sendRedirect("/registro?error=" + java.net.URLEncoder.encode(message, java.nio.charset.StandardCharsets.UTF_8));
    }
}