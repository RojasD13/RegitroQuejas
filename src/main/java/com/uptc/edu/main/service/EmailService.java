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
            
            String contenido = """
                    NOTIFICACIÓN AUTOMÁTICA - SISTEMA DE QUEJAS UPTC
                    ===============================================
                    
                    Se ha habilitado el botón de búsqueda en el sistema.
                    
                    Fecha y hora: %s
                    Dirección IP: %s
                    Navegador: %s
                    
                    Estado: El usuario completó exitosamente la verificación CAPTCHA
                    """.formatted(
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
            
            String contenido = """
                    REPORTE DE BÚSQUEDA - SISTEMA DE QUEJAS UPTC
                    ===========================================
                    
                    Se ha realizado una consulta en el sistema.
                    
                    Fecha y hora: %s
                    Entidad consultada: %s
                    Quejas encontradas: %d
                    Dirección IP: %s
                    
                    Esta información es útil para el análisis de uso del sistema.
                    """.formatted(
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