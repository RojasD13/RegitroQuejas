package com.uptc.edu.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uptc.edu.main.moldel.Empresa;
import com.uptc.edu.main.repository.EmpresaRepo;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepo empresaRepo;

    public List<Empresa> listarEmpresas() {
        return empresaRepo.findAll();
    }

    public Optional<Empresa> buscarPorNombre(String nombre) {
        return empresaRepo.findByNombreEmpresa(nombre);
    }

    public Empresa guardar(Empresa empresa) {
        return empresaRepo.save(empresa);
    }

    public Optional<Empresa> buscarPorId(Long id) {
        return empresaRepo.findById(id);
    }
}
