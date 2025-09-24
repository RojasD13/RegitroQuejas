package com.uptc.edu.main.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ResendEmailService implements SendEmail {

    /* @Value("${RESEND_API_KEY}")
    private String apiKey; */
    @Value("${APP_ADMIN_EMAIL}")
    private String adminEmail;
    @Value("${API_EMAIL}")
    private String fromEmail;

    private final Resend resend;

    public ResendEmailService() throws IOException {
        this.resend = new Resend("re_jYhnfmNz_Vrhf4hVxp8nPwKUxhZ7WKFdA");
    }

    @Override
    public void sendEmail(HttpServletRequest request) throws IOException {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(fromEmail)
                .to(adminEmail)
                .subject("Info Registro Quejas")
                .html(generateHtml(request))
                .build();
        try {
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println(data.getId());
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }

    private String generateHtml(HttpServletRequest request) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"es\">\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>Detalles de la Petición</h1>\n" +
                "        <p><strong>IP:</strong> " + getClientIp(request) + "</p>\n" +
                "        <p><strong>Método HTTP:</strong> " + request.getMethod() + "</p>\n" +
                "        <p><strong>URI de la petición:</strong> " + request.getRequestURI() + "</p>\n" +
                "        <p><strong>Fecha:</strong> " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
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
