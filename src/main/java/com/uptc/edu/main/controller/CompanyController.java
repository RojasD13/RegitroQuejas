package com.uptc.edu.main.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.uptc.edu.main.dto.CompanySummaryDTO;
import com.uptc.edu.main.repository.CompanyRepo;
import com.uptc.edu.main.repository.ComplaintRepo;
import com.uptc.edu.main.service.SendEmail;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CompanyController {

    @Autowired
    private CompanyRepo companyRepo;

    @Autowired
    private ComplaintRepo complaintRepo;

    @Autowired
    private final SendEmail sendEmail;

    public CompanyController(SendEmail sendEmail) {
        this.sendEmail = sendEmail;
    }

    @GetMapping("/analisis")
    public String showAnalisisSummary(Model model, HttpServletRequest request) throws IOException {
        List<CompanySummaryDTO> summary = getTotalComplaintsByCompanies();
        model.addAttribute("resumen", summary);
        sendEmail.sendEmail(request);
        return "analisis";
    }

    private List<CompanySummaryDTO> getTotalComplaintsByCompanies() {
        List<CompanySummaryDTO> summary = companyRepo.findAllByOrderByNameAsc()
                .stream()
                .map(company -> {
                    CompanySummaryDTO dto = new CompanySummaryDTO();
                    dto.setId(company.getId());
                    dto.setCompanyName(company.getName());
                    dto.setTotalComplaints(
                            (long) complaintRepo.findByCompanyIdAndIsVisibleTrue(company.getId()).size());
                    return dto;
                })
                .collect(Collectors.toList());
        return summary;
    }

}
