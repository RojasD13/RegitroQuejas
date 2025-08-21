package com.uptc.edu.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uptc.edu.main.model.Empresa;
import com.uptc.edu.main.model.Queja;

public interface QuejaRepo extends JpaRepository<Queja, Long> {
    List<Queja> findByEmpresaId(Long empresaId);
       List<Queja> findByEmpresa(Empresa empresa);

}
