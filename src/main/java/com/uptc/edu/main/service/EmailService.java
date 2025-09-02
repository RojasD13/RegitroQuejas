package com.uptc.edu.main.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;

    @Value("${app.admin.email:andres.vargas02@uptc.edu.co}")
    private String adminEmail;

    @Value("${spring.mail.username:andres.vargas02@uptc.edu.co}")
    private String fromEmail;

    public void enviarNotificacionBotonHabilitado(String ipUsuario, String userAgent) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(adminEmail);
            message.setSubject("Botón de Búsqueda Habilitado - Sistema de Quejas UPTC");
            
            String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            
            String contenido = String.format(
                "NOTIFICACIÓN AUTOMÁTICA - SISTEMA DE QUEJAS UPTC\n" +
                "===============================================\n\n" +
                "Se ha habilitado el botón de búsqueda en el sistema.\n\n" +
                "Fecha y hora: %s\n" +
                "Dirección IP: %s\n" +
                "Navegador: %s\n\n" +
                "Estado: El usuario completó exitosamente la verificación CAPTCHA\n" +

                fechaHora,
                ipUsuario != null ? ipUsuario : "No disponible",
                userAgent != null ? userAgent.substring(0, Math.min(userAgent.length(), 50)) + "..." : "No disponible"
            );
            
            message.setText(contenido);
            
            emailSender.send(message);
            logger.info("Notificación enviada exitosamente a {}", adminEmail);
            
        } catch (Exception e) {
            logger.error("Error al enviar notificación por email: {}", e.getMessage(), e);
        }
    }

    public void enviarNotificacionBusquedaRealizada(String entidadSeleccionada, int numeroQuejas, String ipUsuario) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(adminEmail);
            message.setSubject("Búsqueda de Quejas Realizada - Sistema UPTC");
            
            String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            
            String contenido = String.format(
                "REPORTE DE BÚSQUEDA - SISTEMA DE QUEJAS UPTC\n" +
                "===========================================\n\n" +
                "Se ha realizado una consulta en el sistema.\n\n" +
                "Fecha y hora: %s\n" +
                "Entidad consultada: %s\n" +
                "Quejas encontradas: %d\n" +
                "Dirección IP: %s\n\n" +
                "Esta información es útil para el análisis de uso del sistema.\n\n" +
                fechaHora,
                entidadSeleccionada,
                numeroQuejas,
                ipUsuario != null ? ipUsuario : "No disponible"
            );
            
            message.setText(contenido);
            
            emailSender.send(message);
            logger.info("Notificación de búsqueda enviada exitosamente");
            
        } catch (Exception e) {
            logger.error("Error al enviar notificación de búsqueda: {}", e.getMessage(), e);
        }
    }
}