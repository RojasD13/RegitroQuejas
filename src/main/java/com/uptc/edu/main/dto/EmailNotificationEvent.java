package com.uptc.edu.main.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmailNotificationEvent {
    
    private String userEmail;
    private String clientIp;
    private String httpMethod;
    private String requestUri;
    private LocalDateTime timestamp;

}
