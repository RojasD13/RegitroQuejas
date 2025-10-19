package com.uptc.edu.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uptc.edu.main.dto.CommentDTO;
import com.uptc.edu.main.model.Company;
import com.uptc.edu.main.service.ApiService;
import com.uptc.edu.main.service.CommentService;
import com.uptc.edu.main.service.CompanyService;
import com.uptc.edu.main.service.ComplaintService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@Validated
public class ComplaintController {
    @Autowired
    private ComplaintService complaintService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ApiService apiService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/auth/login")
    public String showLoginForm(@RequestParam("email") String email,
            @RequestParam("password") String password,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        try {
            apiService.login(email, password);
            session.setAttribute("userEmail", email);
            redirectAttributes.addFlashAttribute("message", "Inicio de sesión exitoso. ¡Bienvenido!");
            return "redirect:/quejas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Correo o contraseña incorrectos");
            return "redirect:/login";
        }
    }

    @PostMapping("/auth/logout")
    public String showLogoutForm(String email) {
        return "registro";
    }

    @GetMapping("/registro")
    public String showForm(Model model) {
        model.addAttribute("entidades", getCompanyNames());
        return "registro";
    }

    @PostMapping("/enviar-queja")
    public String registerComplaint(@RequestParam("entidad") String companyName, @RequestParam String descripcion,
            Model model) {
        companyService.createComplaintForExistingCompany(companyName, descripcion, model);
        model.addAttribute("entidades", getCompanyNames());
        return "registro";
    }

    @GetMapping("/quejas")
    public String showComplaintsByCompany(@RequestParam(required = false) Long companyId, Model model) {
        model.addAttribute("entidades", companyService.listCompanies());
        model.addAttribute("quejas", complaintService.obtainVisibleComplaints(companyId));
        return "buscar";
    }

    @PatchMapping("/quejas/{id}/ocultar")
    public String hideComplaint(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        complaintService.hideComplaintIfExists(id, redirectAttributes, session);
        return redirectToComplaints(session);
    }

    @PostMapping("/buscar-quejas")
    public String searchComplaints(@RequestParam("entidad") Long entidadId, Model model, HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("entidadId", entidadId);
        return "redirect:/ver-quejas";
    }

    @GetMapping("/ver-quejas")
    public String getComplaints(@RequestParam("entidadId") Long entidadId, Model model, HttpServletRequest request) {
        model.addAttribute("entidades", companyService.findAll());
        companyService.getCompanyComplaintsAndSendNotification(entidadId, model, request);
        return "buscar";
    }

    @PostMapping("/api/quejas/{id}/comentarios")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long id, @Valid @RequestBody CommentDTO commentDTO) {
        try {
            var comment = commentService.addCommentToComplaint(id, commentDTO.getText());
            return ResponseEntity.ok(new CommentDTO(comment.getId(), comment.getContent(), comment.getDate()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/api/quejas/{id}/comentarios")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(commentService.getCommentResponsesByComplaintId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/quejas/{id}/cambiar-estado")
    public String changeComplaintState(@PathVariable Long id, @RequestParam String state,
            RedirectAttributes redirectAttributes, HttpSession session) {
        complaintService.changeComplaintState(id, state, redirectAttributes, session);

        return redirectToComplaints(session);
    }

    private List<String> getCompanyNames() {
        return companyService.listCompanies().stream().map(Company::getName).toList();
    }

    private String redirectToComplaints(HttpSession session) {
        Long companyId = (Long) session.getAttribute("ultimaEmpresaBuscada");
        return "redirect:/quejas" + (companyId != null ? "?companyId=" + companyId : "");
    }

}