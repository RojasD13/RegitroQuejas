package com.uptc.edu.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.uptc.edu.main.service.CompanyService;
import com.uptc.edu.main.service.SendEmail;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CompanyController {
    private final CompanyService companyService;
    private final SendEmail sendEmail;

    public CompanyController(CompanyService companyService, SendEmail sendEmail) {
        this.companyService = companyService;
        this.sendEmail = sendEmail;
    }

    @GetMapping("/analisis")
    public String showAnalisisSummary(Model model, HttpServletRequest request) {
        model.addAttribute("resumen", companyService.getCompanySummaries());
        sendEmail.sendEmail(request);
        return "analisis";
    }
}