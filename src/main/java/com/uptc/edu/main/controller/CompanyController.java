package com.uptc.edu.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.uptc.edu.main.service.CompanyService;
import com.uptc.edu.main.service.EmailProducerService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CompanyController {
    
    private final CompanyService companyService;

    @Autowired
    private EmailProducerService producerService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/analisis")
    public String showAnalisisSummary(Model model, HttpServletRequest request) {
        model.addAttribute("resumen", companyService.getCompanySummaries());
        producerService.sendNotification(request);
        return "analisis";
    }
}