package com.uptc.edu.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uptc.edu.main.model.Company;
import com.uptc.edu.main.repository.EmpresaRepo;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepo empresaRepo;

    public List<Company> listarEmpresas() {
        return empresaRepo.findAll();
    }

    public Optional<Company> buscarPorNombre(String nombre) {
        return empresaRepo.findByNombreEmpresa(nombre);
    }

    public Company guardar(Company empresa) {
        return empresaRepo.save(empresa);
    }

    public Optional<Company> buscarPorId(Long id) {
        return empresaRepo.findById(id);
    }
}
