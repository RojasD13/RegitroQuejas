package com.uptc.edu.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uptc.edu.main.model.Company;
import com.uptc.edu.main.model.Complaint;
import com.uptc.edu.main.repository.CompanyRepo;
import com.uptc.edu.main.repository.ComplaintRepo;
import com.uptc.edu.main.service.CompanyService;
import com.uptc.edu.main.service.SendEmail;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ComplaintController {

    @Autowired
    private ComplaintRepo complaintRepo;

    @Autowired
    private CompanyRepo companyRepo;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private final SendEmail sendEmail;

    public ComplaintController(SendEmail sendEmail) {
        this.sendEmail = sendEmail;
    }

    @GetMapping("/registro")
    public String showForm(Model model) {
        model.addAttribute("entidades", getCompanyNames());
        return "registro";
    }

    @PostMapping("/enviar-queja")
    public String registerComplaint(
            @RequestParam("entidad") String companyName,
            @RequestParam String descripcion,
            Model model) {

        companyService.searchByName(companyName).ifPresentOrElse(company -> {
            Complaint complaint = new Complaint();
            complaint.setDescription(descripcion);
            complaint.setCompany(company);
            complaintRepo.save(complaint);

            addMessage(model, "La queja fue registrada exitosamente.", "success");
        }, () -> {
            addMessage(model, "Error: empresa no encontrada.", "error");
        });

        model.addAttribute("entidades", getCompanyNames());
        return "registro";
    }

    private List<String> getCompanyNames() {
        return companyRepo.findAll()
                .stream()
                .map(Company::getName)
                .toList();
    }

    private void addMessage(Model model, String message, String type) {
        model.addAttribute("mensaje", message);
        model.addAttribute("tipoMensaje", type);
    }

    @GetMapping("/quejas")
    public String showComplaintsByCompany(@RequestParam(required = false) Long companyId, Model model) {
        model.addAttribute("entidades", companyRepo.findAll());
        model.addAttribute("quejas", obtainVisibleComplaints(companyId));
        return "buscar";
    }

    private List<Complaint> obtainVisibleComplaints(Long companyId) {
        return (companyId == null)
                ? complaintRepo.findByIsVisibleTrue()
                : complaintRepo.findByCompanyIdAndIsVisibleTrue(companyId);
    }

    @PatchMapping("/quejas/{id}/ocultar")
    public String hideComplaint(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        complaintRepo.findById(id).ifPresentOrElse(complaint -> {
            complaint.setVisible(false);
            complaintRepo.save(complaint);

            redirectAttributes.addFlashAttribute("mensaje", "Queja eliminada exitosamente");
            session.setAttribute("ultimaEmpresaBuscada", complaint.getCompany().getId());
        }, () -> {
            redirectAttributes.addFlashAttribute("error", "La queja no existe");
        });

        Long companyId = (Long) session.getAttribute("ultimaEmpresaBuscada");
        return "redirect:/quejas" + (companyId != null ? "?companyId=" + companyId : "");
    }

    @PostMapping("/buscar-quejas")
    public String buscarQuejas(
            @RequestParam("entidad") Long entidadId,
            Model model,
            HttpServletRequest request) {

        model.addAttribute("entidades", companyRepo.findAll());

        companyRepo.findById(entidadId).ifPresentOrElse(company -> {
            List<Complaint> complaint = complaintRepo.findByCompanyIdAndIsVisibleTrue(company.getId());
            model.addAttribute("quejas", complaint);
            model.addAttribute("entidadSeleccionada", company.getName());
            // sendSearchNotification(company.getName(), request);
            sendEmail.sendEmail(request);
        }, () -> {
            model.addAttribute("quejas", List.of());
            model.addAttribute("entidadSeleccionada", "Entidad no encontrada");
        });

        return "buscar";
    }

    /*
     * private void sendSearchNotification(String companyName, HttpServletRequest
     * request) {
     * emailService.sendNotificationSearchCompleted(
     * companyName,
     * obtenerIpCliente(request),
     * request.getMethod(),
     * request.getRequestURI());
     * }
     */

}
