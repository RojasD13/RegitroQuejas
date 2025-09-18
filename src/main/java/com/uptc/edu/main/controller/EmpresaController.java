package com.uptc.edu.main.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.uptc.edu.main.dto.CompanySummaryDTO;
import com.uptc.edu.main.repository.CompanyRepo;
import com.uptc.edu.main.repository.ComplaintRepo;

@Controller
public class EmpresaController {
    
    @Autowired
    private CompanyRepo empresaRepo;

    @Autowired
    private ComplaintRepo quejaRepo;

    @GetMapping("/analisis")
    public String obtenerTotalQuejasPorEmpresas(Model model) {
        List<CompanySummaryDTO> resumen = empresaRepo.findAllCompaniesOrderByNameAsc()
            .stream()
            .map(empresa -> {
                CompanySummaryDTO dto = new CompanySummaryDTO();
                dto.setId(empresa.getId());
                dto.setCompanyName(empresa.getName());
                dto.setTotalComplaints((long) quejaRepo.findByCompanyIdAndIsVisibleTrue(empresa.getId()).size());
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
