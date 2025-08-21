package com.uptc.edu.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uptc.edu.main.model.Empresa;
import com.uptc.edu.main.model.Queja;
import com.uptc.edu.main.repository.QuejaRepo;

@Service
public class QuejaService {

    @Autowired
    private QuejaRepo quejaRepo;

    // Guardar una queja
    public Queja guardarQueja(Queja queja) {
        return quejaRepo.save(queja);
    }

    // Listar todas las quejas
    public List<Queja> listarQuejas() {
        return quejaRepo.findAll();
    }

    // Buscar quejas por empresa
    public List<Queja> obtenerQuejasPorEmpresa(Empresa empresa) {
        return quejaRepo.findByEmpresa(empresa);
    }

    // Buscar queja por id
    public Optional<Queja> buscarPorId(Long id) {
        return quejaRepo.findById(id);
    }
}
