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
import com.uptc.edu.main.service.CompanyService;
import com.uptc.edu.main.service.ComplaintService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private CompanyService companyService;

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

        companyService.createComplaintForExistingCompany(companyName, descripcion, model);

        model.addAttribute("entidades", getCompanyNames());
        return "registro";
    }

    private List<String> getCompanyNames() {
        return companyService.listCompanies()
                .stream()
                .map(Company::getName)
                .toList();
    }

    @GetMapping("/quejas")
    public String showComplaintsByCompany(@RequestParam(required = false) Long companyId, Model model) {
        model.addAttribute("entidades", companyService.listCompanies());
        model.addAttribute("quejas", complaintService.obtainVisibleComplaints(companyId));
        return "buscar";
    }

    @PatchMapping("/quejas/{id}/ocultar")
    public String hideComplaint(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        complaintService.hideComplaintIfExists(id, redirectAttributes, session);

        Long companyId = (Long) session.getAttribute("ultimaEmpresaBuscada");
        return "redirect:/quejas" + (companyId != null ? "?companyId=" + companyId : "");
    }

    @PostMapping("/buscar-quejas")
    public String buscarQuejas(
            @RequestParam("entidad") Long entidadId,
            Model model,
            HttpServletRequest request) {

        model.addAttribute("entidades", companyService.findAll());

        companyService.getCompanyComplaintsAndSendNotification(entidadId, model, request);

        return "buscar";
    }
}
