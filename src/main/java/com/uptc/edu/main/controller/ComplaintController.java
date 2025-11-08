package com.uptc.edu.main.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uptc.edu.main.dto.CommentDTO;
import com.uptc.edu.main.model.Company;
import com.uptc.edu.main.service.ApiService;
import com.uptc.edu.main.service.CommentService;
import com.uptc.edu.main.service.CompanyService;
import com.uptc.edu.main.service.ComplaintService;
import com.uptc.edu.main.service.EmailProducerService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@Validated
public class ComplaintController {
    private final ComplaintService complaintService;
    private final CompanyService companyService;
    private final CommentService commentService;
    private final ApiService apiService;

    @Autowired
    private EmailProducerService producerService;

    public ComplaintController(ComplaintService complaintService, CompanyService companyService, 
                              CommentService commentService, ApiService apiService) {
        this.complaintService = complaintService;
        this.companyService = companyService;
        this.commentService = commentService;
        this.apiService = apiService;
    }

    @GetMapping({"/login", "/error"})
    public String showLoginForm() {
        return "login";
    }
    @PostMapping("/auth/login")
    public String login(@RequestParam String email, @RequestParam String password, 
                       RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            String response = apiService.login(email, password);
            if (response != null && !response.contains("error")) {
                session.setAttribute("userEmail", email);
                redirectAttributes.addFlashAttribute("message", ApiService.LOGIN_SUCCESS);
                return "redirect:/quejas";
            }
            redirectAttributes.addFlashAttribute("error", ApiService.INVALID_CREDENTIALS);
            return "redirect:/login";

        } catch (ApiService.AuthServiceException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage()); 
            return "redirect:/login";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error inesperado. Intente de nuevo.");
            return "redirect:/login";
        }
    }

    @GetMapping("/auth/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("userEmail");
        if (email != null) {
            try { apiService.logout(email); } catch (Exception e) {
            }
            session.removeAttribute("userEmail");
        }
        redirectAttributes.addFlashAttribute("message", ApiService.LOGOUT_SUCCESS);
        return "redirect:/login";
    }

    @GetMapping("/api/user/status")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUserStatus(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String email = (String) session.getAttribute("userEmail");
        
        if (email != null && !email.isBlank()) {
            try {
                if ("true".equals(apiService.isLogin(email))) {
                    response.put("loggedIn", true);
                    response.put("username", email);
                    return ResponseEntity.ok(response);
                }
            } catch (Exception e) {
            }
        }
        
        response.put("loggedIn", false);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/registro")
    public String showRegistroForm(@RequestParam(required = false) String error, Model model) {
        model.addAttribute("entidades", getCompanyNames());
        if (error != null && !error.isBlank()) {
            model.addAttribute("error", error);
        }
        return "registro";
    }
    @PostMapping("/enviar-queja")
    public String registerComplaint(@RequestParam String entidad, @RequestParam String descripcion, Model model) {
        companyService.createComplaintForExistingCompany(entidad, descripcion, model);
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
    public String searchComplaints(@RequestParam("entidad") Long entidadId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("entidadId", entidadId);
        return "redirect:/ver-quejas";
    }
    @GetMapping("/ver-quejas")
    public String getComplaints(@RequestParam Long entidadId, Model model, HttpServletRequest request) {
        model.addAttribute("entidades", companyService.findAll());
        companyService.getCompanyComplaints(entidadId, model);
        producerService.sendNotification(request);
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
    @PostMapping("/api/notificar-captcha-completado")
    @ResponseBody
    public ResponseEntity<String> notificarCaptchaCompletado() {
        return ResponseEntity.ok("Notificación recibida correctamente");
    }
    private List<String> getCompanyNames() {
        return companyService.listCompanies().stream().map(Company::getName).toList();
    }
    private String redirectToComplaints(HttpSession session) {
        Long companyId = (Long) session.getAttribute("ultimaEmpresaBuscada");
        return "redirect:/quejas" + (companyId != null ? "?companyId=" + companyId : "");
    }
}