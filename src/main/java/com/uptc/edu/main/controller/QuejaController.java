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
import com.uptc.edu.main.repository.EmpresaRepo;
import com.uptc.edu.main.repository.QuejaRepo;
import com.uptc.edu.main.service.EmailService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class QuejaController {

    @Autowired
    private QuejaRepo quejaRepo;

    @Autowired
    private EmpresaRepo empresaRepo;

    @Autowired
    private EmailService emailService;

    @GetMapping("/registro")
    public String mostrarFormulario(Model model) {
        List<Company> empresas = empresaRepo.findAll();

        model.addAttribute("entidades", empresas.stream()
                .map(Company::getName)
                .toList());

        return "registro";
    } 

    @PostMapping("/enviar-queja")
    public String registrarQueja(
            @RequestParam("entidad") String nombreEmpresa,
            @RequestParam String descripcion,
            Model model) {

        empresaRepo.findByNombreEmpresa(nombreEmpresa).ifPresentOrElse(empresa -> {
            Complaint queja = new Complaint();
            queja.setDescription(descripcion);
            queja.setCompany(empresa);
            quejaRepo.save(queja);

            model.addAttribute("mensaje", "La queja fue registrada exitosamente.");
            model.addAttribute("tipoMensaje", "success");
        }, () -> {
            model.addAttribute("mensaje", "Error: empresa no encontrada.");
            model.addAttribute("tipoMensaje", "error");
        });

        model.addAttribute("entidades",
                empresaRepo.findAll().stream()
                        .map(Company::getName)
                        .toList());

        return "registro";
    }

    @GetMapping("/quejas")
    public String mostrarQuejasporEmpresa(@RequestParam(required = false) Long empresaId, Model model) {
        model.addAttribute("entidades", empresaRepo.findAll());

        List<Complaint> quejas = (empresaId == null)
                ? quejaRepo.findByIsVisibleTrue()
                : quejaRepo.findByEmpresaIdAndIsVisibleTrue(empresaId);

        model.addAttribute("quejas", quejas);
        return "buscar";
    }

    @PatchMapping("/quejas/{id}/ocultar")
    public String ocultarQueja(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        quejaRepo.findById(id).ifPresentOrElse(queja -> {
            queja.setVisible(false);
            quejaRepo.save(queja);

            redirectAttributes.addFlashAttribute("mensaje", "Queja eliminada exitosamente");
            session.setAttribute("ultimaEmpresaBuscada", queja.getCompany().getId());
        }, () -> {
            redirectAttributes.addFlashAttribute("error", "La queja no existe");
        });

        Long empresaId = (Long) session.getAttribute("ultimaEmpresaBuscada");
        return "redirect:/quejas" + (empresaId != null ? "?empresaId=" + empresaId : "");
    }

    @PostMapping("/buscar-quejas")
    public String buscarQuejas(
            @RequestParam("entidad") Long empresaId,
            Model model,
            HttpServletRequest request) {

        model.addAttribute("entidades", empresaRepo.findAll());

        empresaRepo.findById(empresaId).ifPresentOrElse(empresa -> {
            List<Complaint> quejas = quejaRepo.findByEmpresaIdAndIsVisibleTrue(empresa.getId());
            model.addAttribute("quejas", quejas);
            model.addAttribute("entidadSeleccionada", empresa.getName());

            emailService.enviarNotificacionBusquedaRealizada(
                    empresa.getName(),
                    obtenerIpCliente(request),
                    request.getMethod(),
                    request.getRequestURI());
        }, () -> {
            model.addAttribute("quejas", List.of());
            model.addAttribute("entidadSeleccionada", null);
        });

        return "buscar";
    }

    private String obtenerIpCliente(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }

        return request.getRemoteAddr();
    }
}
