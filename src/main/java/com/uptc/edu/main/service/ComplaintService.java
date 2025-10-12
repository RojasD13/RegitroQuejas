package com.uptc.edu.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uptc.edu.main.model.Company;
import com.uptc.edu.main.model.Complaint;
import com.uptc.edu.main.model.State;
import com.uptc.edu.main.repository.ComplaintRepo;

import jakarta.servlet.http.HttpSession;
@Service
public class ComplaintService {
    @Autowired private ComplaintRepo complaintRepo;
    public Complaint saveComplaint(Complaint complaint) {
        return complaintRepo.save(complaint);
    }
    public List<Complaint> listComplaints() {
        return complaintRepo.findAll();
    }
    public List<Complaint> getComplaintsByCompany(Company company) {
        return complaintRepo.findByCompany(company);
    }
    public Optional<Complaint> searchById(Long id) {
        return complaintRepo.findById(id);
    }
    public List<Complaint> obtainVisibleComplaints(Long companyId) {
        return (companyId == null)
                ? complaintRepo.findByIsVisibleTrue()
                : complaintRepo.findByCompanyIdAndIsVisibleTrue(companyId);
    }
    public void hideComplaintIfExists(Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        complaintRepo.findById(id).ifPresentOrElse(complaint -> {
            complaint.setVisible(false);
            complaintRepo.save(complaint);
            redirectAttributes.addFlashAttribute("mensaje", "Queja eliminada exitosamente");
            session.setAttribute("ultimaEmpresaBuscada", complaint.getCompany().getId());
        }, () -> redirectAttributes.addFlashAttribute("error", "La queja no existe"));
    }
    public void changeComplaintState(Long id, String state, RedirectAttributes redirectAttributes, HttpSession session) {
    complaintRepo.findById(id).ifPresentOrElse(complaint -> {
        try {
            State newState = State.valueOf(state.toUpperCase());
            complaint.setState(newState);
            complaintRepo.save(complaint);
            redirectAttributes.addFlashAttribute("mensaje", "Estado de la queja actualizado exitosamente");
            session.setAttribute("ultimaEmpresaBuscada", complaint.getCompany().getId());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Estado no válido");
        }
    }, () -> {
        redirectAttributes.addFlashAttribute("error", "La queja no existe");
    });
}
}