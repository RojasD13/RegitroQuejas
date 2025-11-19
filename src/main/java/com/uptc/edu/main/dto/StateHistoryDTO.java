package com.uptc.edu.main.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class StateHistoryDTO {
    private String state;
    private LocalDateTime timestamp;

    public StateHistoryDTO(String state, LocalDateTime timestamp) {
        this.state = state;
        this.timestamp = timestamp;
    }
}