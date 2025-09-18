package com.uptc.edu.main.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uptc.edu.main.service.EmailService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class NotificacionController {

    private final EmailService emailService;

    @Autowired
    public NotificacionController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/notificar-captcha-completado")
    public ResponseEntity<Map<String, String>> notificarCaptchaCompletado(
            HttpServletRequest request,
            @RequestParam(name = "entidad", required = false, defaultValue = "CaptchaService") String entidad) {

        String ipUsuario = obtenerIpCliente(request);
        String httpMethod = request.getMethod();
        String requestUri = request.getRequestURI();

        // Llamada asíncrona al servicio de email
        emailService.sendNotificationSearchCompleted(entidad, ipUsuario, httpMethod, requestUri);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Notificación en proceso");
        return ResponseEntity.ok(response);
    }

    private String obtenerIpCliente(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }

        return request.getRemoteAddr();
    }
}
