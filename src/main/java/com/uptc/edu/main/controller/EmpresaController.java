package com.uptc.edu.main.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.uptc.edu.main.dto.EmpresaResumenDTO;
import com.uptc.edu.main.repository.EmpresaRepo;
import com.uptc.edu.main.repository.QuejaRepo;

@Controller
public class EmpresaController {
    
    @Autowired
    private EmpresaRepo empresaRepo;

    @Autowired
    private QuejaRepo quejaRepo;

    @GetMapping("/analisis")
    public String obtenerTotalQuejasPorEmpresas(Model model) {
        List<EmpresaResumenDTO> resumen = empresaRepo.findAllByOrderByNombreEmpresaAsc()
            .stream()
            .map(empresa -> {
                EmpresaResumenDTO dto = new EmpresaResumenDTO();
                dto.setId(empresa.getId());
                dto.setNombreEmpresa(empresa.getName());
                dto.setTotalQuejas((long) quejaRepo.findByEmpresaIdAndIsVisibleTrue(empresa.getId()).size());
                return dto;
            })
            .collect(Collectors.toList());
        model.addAttribute("resumen", resumen);

        /* // Calcular totales basados en quejas visibles
        long totalQuejas = resumen.stream().mapToLong(EmpresaResumenDTO::getTotalQuejas).sum();
        int totalEntidades = resumen.size();
        long promedio = totalEntidades > 0 ? totalQuejas / totalEntidades : 0;

        // Agregar atributos al modelo
        
        model.addAttribute("totalQuejas", totalQuejas);
        model.addAttribute("totalEntidades", totalEntidades);
        model.addAttribute("promedio", promedio); */

        return "analisis";
    }

}
