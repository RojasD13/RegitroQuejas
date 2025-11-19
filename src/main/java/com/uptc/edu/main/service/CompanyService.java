package com.uptc.edu.main.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.uptc.edu.main.repository.ComplaintRepo;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.uptc.edu.main.dto.CompanySummaryDTO;

import com.uptc.edu.main.model.Company;
import com.uptc.edu.main.model.Complaint;

import com.uptc.edu.main.repository.CompanyRepo;

@Service
public class CompanyService {

    private final ComplaintRepo complaintRepo;
    
    private final CompanyRepo companyRepo;

    CompanyService(ComplaintRepo complaintRepo, CompanyRepo companyRepo) {
        this.complaintRepo = complaintRepo;
        this.companyRepo = companyRepo;
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

    public void getCompanyComplaints(Long entidadId, Model model) {
        companyRepo.findById(entidadId).ifPresentOrElse(company -> {
            List<Complaint> complaint = complaintRepo.findByCompanyIdAndIsVisibleTrue(company.getId());
            model.addAttribute("quejas", complaint);
            model.addAttribute("entidadSeleccionada", company.getName());            
        }, () -> {
            model.addAttribute("quejas", List.of());
            model.addAttribute("entidadSeleccionada", "Entidad no encontrada");
        });
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
