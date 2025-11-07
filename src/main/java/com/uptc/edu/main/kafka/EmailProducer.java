package com.uptc.edu.main.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.uptc.edu.main.dto.EmailNotificationEvent;

@Service
public class EmailProducer {

    private final KafkaTemplate<String, EmailNotificationEvent> kafkaTemplate;

    public EmailProducer(KafkaTemplate<String, EmailNotificationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEmailEvent(EmailNotificationEvent event) {
        kafkaTemplate.send("email-notifications", event);
        System.out.println("Evento enviado a Kafka: " + event.getRequestUri());
    }
}
