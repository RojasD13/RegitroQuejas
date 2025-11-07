package com.uptc.edu.main.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.uptc.edu.main.repository.ComplaintRepo;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.uptc.edu.main.dto.CompanySummaryDTO;
import com.uptc.edu.main.dto.EmailNotificationEvent;

import com.uptc.edu.main.kafka.EmailProducer;

import com.uptc.edu.main.model.Company;
import com.uptc.edu.main.model.Complaint;

import com.uptc.edu.main.repository.CompanyRepo;

@Service
public class CompanyService {

    private final ComplaintRepo complaintRepo;

    @Autowired
    private CompanyRepo companyRepo;

    @Autowired
    private EmailNotificationEvent event;

    @Autowired
    private EmailProducer emailProducer;

    CompanyService(ComplaintRepo complaintRepo) {
        this.complaintRepo = complaintRepo;
    }

    public List<Company> listCompanies() {
        return companyRepo.findAll();
    }

    public Optional<Company> searchByName(String name) {
        return companyRepo.findByName(name);
    }

    public Company save(Company company) {
        return companyRepo.save(company);
    }

    public Optional<Company> searchById(Long id) {
        return companyRepo.findById(id);
    }

    public List<Company> findAllByOrderByNameAsc() {
        return companyRepo.findAllByOrderByNameAsc();
    }

    public List<Company> findAll() {
        return companyRepo.findAll();
    }

    public void createComplaintForExistingCompany(String companyName, String descripcion, Model model) {
        companyRepo.findByName(companyName).ifPresentOrElse(company -> {
            Complaint complaint = new Complaint();
            complaint.setDescription(descripcion);
            complaint.setCompany(company);
            complaintRepo.save(complaint);
            addMessage(model, "La queja fue registrada exitosamente.", "success");
        }, () -> {
            addMessage(model, "Error: empresa no encontrada.", "error");
        });
    }

    private void addMessage(Model model, String message, String type) {
        model.addAttribute("mensaje", message);
        model.addAttribute("tipoMensaje", type);
    }

    public void getCompanyComplaintsAndSendNotification(Long entidadId, Model model, HttpServletRequest request) {
        companyRepo.findById(entidadId).ifPresentOrElse(company -> {
            List<Complaint> complaint = complaintRepo.findByCompanyIdAndIsVisibleTrue(company.getId());
            model.addAttribute("quejas", complaint);
            model.addAttribute("entidadSeleccionada", company.getName());
            emailProducer.sendEmailEvent(initEvent(request));
        }, () -> {
            model.addAttribute("quejas", List.of());
            model.addAttribute("entidadSeleccionada", "Entidad no encontrada");
        });
    }

    private EmailNotificationEvent initEvent(HttpServletRequest request) {
        event.setUserEmail(getUserEmail(request));
        event.setClientIp(getClientIp(request));
        event.setHttpMethod(request.getMethod());
        event.setRequestUri(request.getRequestURI());
        event.setTimestamp(LocalDateTime.now());
        return event;
    }

    private String getUserEmail(HttpServletRequest request) {
        if (request.getSession().getAttribute("userEmail")==null) {
            return "Usuario público";        
        }
        return (String) request.getSession().getAttribute("userEmail");
    }

     private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isBlank()) {
            return ip.split(",")[0].trim();
        }
        ip = request.getHeader("X-Real-IP");
        return (ip != null && !ip.isBlank()) ? ip : request.getRemoteAddr();
    }

    public List<CompanySummaryDTO> getCompanySummaries() {
        return companyRepo.findAllByOrderByNameAsc()
                .stream()
                .map(company -> {
                    CompanySummaryDTO dto = new CompanySummaryDTO();
                    dto.setId(company.getId());
                    dto.setCompanyName(company.getName());
                    dto.setTotalComplaints(
                            (long) complaintRepo.findByCompanyIdAndIsVisibleTrue(company.getId()).size());
                    return dto;
                }).collect(Collectors.toList());
    }
}
