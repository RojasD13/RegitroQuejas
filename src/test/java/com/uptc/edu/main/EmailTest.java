package com.uptc.edu.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.uptc.edu.main.service.EmailService;

@Component
@Profile("dev")
public class EmailTest implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(EmailTest.class);

    private final EmailService emailService;

    @Autowired
    public EmailTest(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Envía un email de prueba (de forma asíncrona). No bloqueará el arranque.
        emailService.sendNotificationSearchCompleted("EntidadPrueba", "127.0.0.1", "GET", "/");
        logger.info("Email de prueba disparado (profile=dev). Revisa logs y la bandeja del admin configurado en app.admin.email.");
    }
}
