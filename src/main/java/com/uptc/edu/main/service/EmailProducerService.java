package com.uptc.edu.main.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import com.uptc.edu.main.dto.EmailNotificationEvent;
import com.uptc.edu.main.kafka.EmailProducer;

import jakarta.servlet.http.HttpServletRequest;

public class EmailProducerService {

    @Autowired
    private EmailProducer emailProducer;
    
    @Autowired
    private EmailNotificationEvent event;

    public void sendNotification(HttpServletRequest request) {
        emailProducer.sendEmailEvent(this.initEvent(request));
    }

    private EmailNotificationEvent initEvent(HttpServletRequest request) {
        event.setUserEmail(getUserEmail(request));
        event.setClientIp(getClientIp(request));
        event.setHttpMethod(request.getMethod());
        event.setRequestUri(request.getRequestURI());
        event.setTimestamp(LocalDateTime.now());
        return event;
    }

    private String getUserEmail(HttpServletRequest request) {
        if (request.getSession().getAttribute("userEmail")==null) {
            return "Usuario público";        
        }
        return (String) request.getSession().getAttribute("userEmail");
    }

     private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isBlank()) {
            return ip.split(",")[0].trim();
        }
        ip = request.getHeader("X-Real-IP");
        return (ip != null && !ip.isBlank()) ? ip : request.getRemoteAddr();
    }

}
