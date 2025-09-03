package com.uptc.edu.main;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import com.uptc.edu.main.service.EmailService;

// Descomenta esta clase solo para probar el envío de emails
//@Component
public class EmailTest implements CommandLineRunner {

    @Autowired
    private EmailService emailService;

    @Override
    public void run(String... args) throws Exception {
        // Prueba el envío de email al iniciar la aplicación
        emailService.enviarNotificacionBotonHabilitado("192.168.1.1", "Chrome/Test");
        System.out.println("Email de prueba enviado");
    }
}