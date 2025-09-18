package com.uptc.edu.main.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private static final DateTimeFormatter TIMESTAMP_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final JavaMailSender emailSender;
    private final String adminEmail;
    private final String fromEmail;
    private final String appName;

    public EmailService(JavaMailSender emailSender,
                        @Value("${app.admin.email}") String adminEmail,
                        @Value("${spring.mail.username}") String fromEmail,
                        @Value("${spring.application.name:RegistroQuejas}") String appName) {
        this.emailSender = Objects.requireNonNull(emailSender, "emailSender");
        this.adminEmail = Objects.requireNonNull(adminEmail, "app.admin.email must be configured");
        this.fromEmail = Objects.requireNonNull(fromEmail, "spring.mail.username must be configured");
        this.appName = (appName == null || appName.isBlank()) ? "RegistroQuejas" : appName;
    }

    @Async
    public void sendNotificationSearchCompleted(
            String selectedEntity,
            String userIp,
            String httpMethod,
            String requestUri) {

        if (httpMethod == null || httpMethod.isBlank()) {
            httpMethod = "UNKNOWN";
        }
        if (requestUri == null || requestUri.isBlank()) {
            requestUri = "No disponible";
        }
        if (selectedEntity == null || selectedEntity.isBlank()) {
            selectedEntity = "No especificada";
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(adminEmail);
            message.setSubject("Búsqueda realizada - Sistema " + appName);

            String fechaHora = LocalDateTime.now().format(TIMESTAMP_FMT);

            String contenido = String.format(
                    "REPORTE DE BÚSQUEDA - %s%n"
                  + "===========================================%n%n"
                  + "Se ha realizado una consulta en el sistema.%n%n"
                  + "Fecha y hora: %s%n"
                  + "Método HTTP: %s%n"
                  + "URI solicitada: %s%n"
                  + "Entidad consultada: %s%n"
                  + "Dirección IP: %s%n%n"
                  + "Esta información es útil para el análisis de uso y seguridad del sistema.%n",
                    appName,
                    fechaHora,
                    httpMethod,
                    requestUri,
                    selectedEntity,
                    (userIp != null && !userIp.isBlank()) ? userIp : "No disponible"
            );

            message.setText(contenido);
            emailSender.send(message);
            logger.info("Notificación de búsqueda enviada exitosamente a {}", adminEmail);

        } catch (Exception e) {
            logger.error("Error al enviar notificación de búsqueda", e);
        }
    }
}
