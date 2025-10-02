package com.uptc.edu.main.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanySummaryDTO {
    private Long id;
    private String companyName;
    private Long totalComplaints;
}