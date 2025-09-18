package com.uptc.edu.main.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanySummaryDTO {
    
    private Long id;
    private String companyName;
    private long totalComplaints;

}
