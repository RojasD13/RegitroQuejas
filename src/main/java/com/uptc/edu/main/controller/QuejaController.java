package com.uptc.edu.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uptc.edu.main.model.Empresa;
import com.uptc.edu.main.model.Queja;
import com.uptc.edu.main.repository.EmpresaRepo;
import com.uptc.edu.main.repository.QuejaRepo;
import com.uptc.edu.main.service.EmailService;

import jakarta.servlet.http.HttpServletRequest;

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
        List<Empresa> empresas = empresaRepo.findAll();

        model.addAttribute("entidades", empresas.stream()
                .map(Empresa::getNombreEmpresa)
                .toList());

        return "registro"; 
    }

    @PostMapping("/enviar-queja")
    public String registrarQueja(
            @RequestParam("entidad") String nombreEmpresa,
            @RequestParam String descripcion,
            Model model) {

        try {
            Empresa empresa = empresaRepo.findByNombreEmpresa(nombreEmpresa)
                    .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

            Queja queja = new Queja();
            queja.setDescripcion(descripcion);
            queja.setEmpresa(empresa);

            quejaRepo.save(queja);

            model.addAttribute("mensaje", "La queja fue registrada exitosamente.");
            model.addAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            model.addAttribute("mensaje", "Error al registrar la queja: " + e.getMessage());
            model.addAttribute("tipoMensaje", "error");
        }

        // volver a cargar la lista de entidades
        List<Empresa> empresas = empresaRepo.findAll();
        model.addAttribute("entidades", empresas.stream()
                .map(Empresa::getNombreEmpresa)
                .toList());

        return "registro"; // recargar la misma vista con mensaje
    }

    @GetMapping("/quejas")
    public String mostrarQuejasporEmpresa(Model model) {
        List<Empresa> empresas = empresaRepo.findAll();
        model.addAttribute("entidades", empresas); // enviamos todas las empresas
        model.addAttribute("quejas", null); // lista vacía al inicio
        return "buscar";
    }

    @PostMapping("/buscar-quejas")
    public String buscarQuejas(
            @RequestParam("entidad") Long empresaId,
            Model model,
            HttpServletRequest request) {

        List<Empresa> empresas = empresaRepo.findAll();
        model.addAttribute("entidades", empresas);

        Empresa empresaSeleccionada = empresaRepo.findById(empresaId)
                .orElse(null);

        if (empresaSeleccionada != null) {
            List<Queja> quejas = quejaRepo.findByEmpresa(empresaSeleccionada);
            model.addAttribute("quejas", quejas);
            model.addAttribute("entidadSeleccionada", empresaSeleccionada.getNombreEmpresa());

            // Enviar notificación por email de forma asíncrona
            String ipUsuario = obtenerIpCliente(request);
            new Thread(() -> {
                emailService.enviarNotificacionBusquedaRealizada(
                    empresaSeleccionada.getNombreEmpresa(), 
                    quejas.size(), 
                    ipUsuario
                );
            }).start();

        } else {
            model.addAttribute("quejas", List.of());
            model.addAttribute("entidadSeleccionada", null);
        }

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