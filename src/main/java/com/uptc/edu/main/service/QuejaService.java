package com.uptc.edu.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uptc.edu.main.model.Empresa;
import com.uptc.edu.main.model.Complaint;
import com.uptc.edu.main.repository.QuejaRepo;

@Service
public class QuejaService {

    @Autowired
    private QuejaRepo quejaRepo;

    // Guardar una queja
    public Complaint guardarQueja(Complaint queja) {
        return quejaRepo.save(queja);
    }

    // Listar todas las quejas
    public List<Complaint> listarQuejas() {
        return quejaRepo.findAll();
    }

    // Buscar quejas por empresa
    public List<Complaint> obtenerQuejasPorEmpresa(Empresa empresa) {
        return quejaRepo.findByEmpresa(empresa);
    }

    // Buscar queja por id
    public Optional<Complaint> buscarPorId(Long id) {
        return quejaRepo.findById(id);
    }
}
