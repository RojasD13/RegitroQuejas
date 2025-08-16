package com.uptc.edu.main.controller;

import com.uptc.edu.main.dto.EmpresaResumenDTO;
import com.uptc.edu.main.repository.EmpresaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {
    
    @Autowired
    private EmpresaRepo empresaRepo;

    @GetMapping("/resumen")
    public ResponseEntity<List<EmpresaResumenDTO>> getEmpresasConTotalQuejas() {
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
        
        return ResponseEntity.ok(resumen);
    }
}
