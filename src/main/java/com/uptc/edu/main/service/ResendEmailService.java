package com.uptc.edu.main.service;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

@Service
public class ResendEmailService implements SendEmail {

    @Value("${RESEND_API_KEY}")
    private String apiKey;
    @Value("${APP_ADMIN_EMAIL}")
    private String adminEmail;
    @Value("${API_EMAIL}")
    private String fromEmail;

    private final Resend resend;

    public ResendEmailService() throws IOException {
        this.resend = new Resend(apiKey);
    }

    @Override
    public void sendEmail(String ip, String httpMethod, String requestUri) {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(fromEmail)
                .to(adminEmail)
                .subject("Info Registro Quejas")
                .html(generateHtml(ip, httpMethod, requestUri))
                .build();
        try {
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println(data.getId());
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }

    private String generateHtml(String ip, String httpMethod, String requestUri) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"es\">\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>Detalles de la Petición</h1>\n" +
                "        <p><strong>IP:</strong> " + ip + "</p>\n" +
                "        <p><strong>Método HTTP:</strong> " + httpMethod + "</p>\n" +
                "        <p><strong>URI de la petición:</strong> " + requestUri + "</p>\n" +
                "        <p><strong>Fecha:</strong> " + LocalDateTime.now() + "</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

}
