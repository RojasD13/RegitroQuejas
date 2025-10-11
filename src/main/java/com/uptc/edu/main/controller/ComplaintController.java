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
    // ENDPOINTS DE API PARA COMENTARIOS     
    @PostMapping("/api/quejas/{id}/comentarios")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentDTO commentDTO) { 
        try {
           
            var comment = commentService.addCommentToComplaint(id, commentDTO.getText());            
            CommentDTO response = new CommentDTO();
            response.setId(comment.getId());
            response.setText(comment.getContent());
            response.setTimestamp(comment.getDate());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }    
    @GetMapping("/api/quejas/{id}/comentarios")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long id) {
        try {
            List<CommentDTO> comments = commentService.getCommentResponsesByComplaintId(id);
            return ResponseEntity.ok(comments);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}