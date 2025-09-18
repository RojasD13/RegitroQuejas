package com.uptc.edu.main.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uptc.edu.main.service.EmailService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class NotificationController {

    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/notificar-captcha-completado")
    public ResponseEntity<Map<String, String>> notifyCaptchaCompleted(
            HttpServletRequest request,
            @RequestParam(name = "entidad", required = false, defaultValue = "CaptchaService") String entity) {

        String userIp = getClientIp(request);
        String httpMethod = request.getMethod();
        String requestUri = request.getRequestURI();

        // Llamada asíncrona al servicio de email
        emailService.sendNotificationSearchCompleted(entity, userIp, httpMethod, requestUri);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Notificación en proceso");
        return ResponseEntity.ok(response);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isBlank())
            return ip.split(",")[0].trim();

        ip = request.getHeader("X-Real-IP");
        return (ip != null && !ip.isBlank()) ? ip : request.getRemoteAddr();
    }

}
