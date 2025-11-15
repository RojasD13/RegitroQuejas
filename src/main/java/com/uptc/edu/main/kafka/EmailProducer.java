package com.uptc.edu.main.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.uptc.edu.main.dto.EmailNotificationEvent;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailProducer {

    private final KafkaTemplate<String, EmailNotificationEvent> kafkaTemplate;

    public EmailProducer(KafkaTemplate<String, EmailNotificationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEmailEvent(EmailNotificationEvent event) {
        kafkaTemplate.send("email-notifications", event);
        log.info("Evento de notificación de correo electrónico enviado a Kafka: {}", event.getRequestUri());        
    }
}
