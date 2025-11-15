package com.uptc.edu.main.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailNotificationEvent implements Serializable {
    
    private String userEmail;
    private String clientIp;
    private String httpMethod;
    private String requestUri;
    private LocalDateTime timestamp;

}
