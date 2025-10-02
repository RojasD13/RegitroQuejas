package com.uptc.edu.main.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.uptc.edu.main.dto.CompanySummaryDTO;
import com.uptc.edu.main.service.CompanyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Controller
@RequiredArgsConstructor 
@Slf4j
public class CompanyController {
    private final CompanyService companyService;
    @GetMapping("/analisis") 
    public String showAnalisisSummary(Model model) {
        log.debug("Displaying analysis summary page");        
        List<CompanySummaryDTO> summary = companyService.getCompanySummaryWithComplaintCount();
        model.addAttribute("resumen", summary);        
        return "analisis";
    }
}