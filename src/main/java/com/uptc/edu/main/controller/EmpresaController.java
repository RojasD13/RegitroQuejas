package com.uptc.edu.main.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.uptc.edu.main.dto.EmpresaResumenDTO;
import com.uptc.edu.main.repository.EmpresaRepo;

@Controller
public class EmpresaController {
    
    @Autowired
    private EmpresaRepo empresaRepo;

    @GetMapping("/analisis")
    public String getEmpresasConTotalQuejas(Model model) {
        List<EmpresaResumenDTO> resumen = empresaRepo.findAllByOrderByNombreEmpresaAsc()
            .stream()
            .map(empresa -> {
                EmpresaResumenDTO dto = new EmpresaResumenDTO();
                dto.setId(empresa.getId());
                dto.setNombreEmpresa(empresa.getNombreEmpresa());
                dto.setTotalQuejas((long) empresa.getQuejas().size());
                return dto;
            })
            .collect(Collectors.toList());

        // total de quejas
        long totalQuejas = resumen.stream().mapToLong(EmpresaResumenDTO::getTotalQuejas).sum();
        // número de entidades
        int totalEntidades = resumen.size();
        // promedio de quejas
        long promedio = totalEntidades > 0 ? totalQuejas / totalEntidades : 0;

        // Se agregan atributos al modelo
        model.addAttribute("resumen", resumen);
        model.addAttribute("totalQuejas", totalQuejas);
        model.addAttribute("totalEntidades", totalEntidades);
        model.addAttribute("promedio", promedio);

        return "analisis"; // renderiza el archivo analisis.html
    }

}
