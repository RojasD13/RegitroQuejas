package com.uptc.edu.main.service;

import java.util.List;
import java.util.Optional;
import com.uptc.edu.main.repository.ComplaintRepo;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.uptc.edu.main.model.Company;
import com.uptc.edu.main.model.Complaint;
import com.uptc.edu.main.repository.CompanyRepo;

@Service
public class CompanyService {

    private final ComplaintRepo complaintRepo;

    @Autowired
    private CompanyRepo companyRepo;
    
    @Autowired
    private final SendEmail sendEmail;

    CompanyService(ComplaintRepo complaintRepo, SendEmail sendEmail) {
        this.complaintRepo = complaintRepo;
        this.sendEmail = sendEmail;
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
            sendEmail.sendEmail(request);
        }, () -> {
            model.addAttribute("quejas", List.of());
            model.addAttribute("entidadSeleccionada", "Entidad no encontrada");
        });
    }
}
