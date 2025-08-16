package com.uptc.edu.main.controller;

import com.uptc.edu.main.moldel.Queja;
import com.uptc.edu.main.dto.QuejaEmpresaDTO;
import com.uptc.edu.main.dto.RegistroQuejaDTO;
import com.uptc.edu.main.moldel.Empresa;
import com.uptc.edu.main.repository.EmpresaRepo;
import com.uptc.edu.main.repository.QuejaRepo;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quejas")
public class QuejaController {

    @Autowired
    private QuejaRepo quejaRepo;

    @Autowired
    private EmpresaRepo empresaRepo;

    @PostMapping
    public ResponseEntity<?> registrarQueja(@RequestBody @Valid RegistroQuejaDTO quejaDTO) {
        try {
            Empresa empresa = empresaRepo.findById(quejaDTO.getEmpresaId())
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada con ID: " + quejaDTO.getEmpresaId()));

            Queja queja = new Queja();
            queja.setDescripcion(quejaDTO.getDescripcion());
            queja.setEmpresa(empresa);

            Queja quejaGuardada = quejaRepo.save(queja);
            
            QuejaEmpresaDTO response = new QuejaEmpresaDTO();
            response.setId(quejaGuardada.getId());
            response.setDescripcion(quejaGuardada.getDescripcion());
            response.setNombreEmpresa(quejaGuardada.getEmpresa().getNombreEmpresa());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<?> getQuejasPorEmpresa(@PathVariable Long empresaId) {
        empresaRepo.findById(empresaId)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        List<Queja> quejas = quejaRepo.findByEmpresaId(empresaId);
        return ResponseEntity.ok(quejas);
    }
}
