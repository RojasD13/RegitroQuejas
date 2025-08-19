package com.uptc.edu.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uptc.edu.main.moldel.Empresa;
import com.uptc.edu.main.moldel.Queja;
import com.uptc.edu.main.repository.EmpresaRepo;
import com.uptc.edu.main.repository.QuejaRepo;

@Controller
public class QuejaController {

    @Autowired
    private QuejaRepo quejaRepo;

    @Autowired
    private EmpresaRepo empresaRepo;

    // 1️⃣ Mostrar el formulario con la lista de empresas
    @GetMapping("/registro")
    public String mostrarFormulario(Model model) {
        List<Empresa> empresas = empresaRepo.findAll();

        // Cargamos la lista de nombres de empresa al modelo
        model.addAttribute("entidades", empresas.stream()
                .map(Empresa::getNombreEmpresa)
                .toList());

        return "registro"; // nombre del HTML (templates/registroQueja.html)
    }





    // 2️⃣ Procesar el formulario
    @PostMapping("/enviar-queja")
    public String registrarQueja(
            @RequestParam("entidad") String nombreEmpresa,
            @RequestParam("descripcion") String descripcion,
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


     // 1️⃣ Mostrar formulario de búsqueda
    @GetMapping("/quejas")
    public String mostrarQuejasporEmpresa(Model model) {
        List<Empresa> empresas = empresaRepo.findAll();
        model.addAttribute("entidades", empresas); // enviamos todas las empresas
        model.addAttribute("quejas", null); // lista vacía al inicio
        return "buscar";
    }

    // 2️⃣ Procesar la búsqueda
    @PostMapping("/buscar-quejas")
    public String buscarQuejas(
            @RequestParam("entidad") Long empresaId,
            Model model) {

        List<Empresa> empresas = empresaRepo.findAll();
        model.addAttribute("entidades", empresas);

        Empresa empresaSeleccionada = empresaRepo.findById(empresaId)
                .orElse(null);

        if (empresaSeleccionada != null) {
            List<Queja> quejas = quejaRepo.findByEmpresa(empresaSeleccionada);
            model.addAttribute("quejas", quejas);
            model.addAttribute("entidadSeleccionada", empresaSeleccionada.getNombreEmpresa());
        } else {
            model.addAttribute("quejas", List.of());
            model.addAttribute("entidadSeleccionada", null);
        }

        return "buscar";
    }
}
