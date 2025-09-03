package com.uptc.edu.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uptc.edu.main.service.EmailService;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class NotificacionController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/notificar-captcha-completado")
    public ResponseEntity<Map<String, String>> notificarCaptchaCompletado(HttpServletRequest request) {
        try {
            // Obtener información del usuario
            String ipUsuario = obtenerIpCliente(request);
            String userAgent = request.getHeader("User-Agent");
            
            // Enviar notificación por email de forma asíncrona
            new Thread(() -> {
                emailService.enviarNotificacionBotonHabilitado(ipUsuario, userAgent);
            }).start();
            
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Notificación enviada correctamente");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error al enviar notificación");
            
            return ResponseEntity.status(500).body(response);
        }
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