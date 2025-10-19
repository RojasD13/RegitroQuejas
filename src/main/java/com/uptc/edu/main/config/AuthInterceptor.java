package com.uptc.edu.main.config;

import com.uptc.edu.main.service.ApiService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final ApiService apiService;

    public AuthInterceptor(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String email = (String) request.getSession().getAttribute("userEmail");
        
        if (email == null || email.isBlank()) {
            handleUnauthorizedRequest(response, "Falta el header X-User-Email");
            return false;
        }

        if (!Boolean.parseBoolean(apiService.isLogin(email))) {
            handleUnauthorizedRequest(response, "Usuario no autorizado o sesión expirada");
            return false;
        }

        return true;
    }

    private void handleUnauthorizedRequest(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(message);
    }
}
