package com.uptc.edu.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uptc.edu.main.model.Empresa;
import com.uptc.edu.main.model.Queja;

public interface QuejaRepo extends JpaRepository<Queja, Long> {

    List<Queja> findByEmpresa(Empresa empresa);

    @Query("SELECT q FROM Queja q WHERE q.empresa.id = :empresaId AND q.isVisible = true")
    List<Queja> findByEmpresaId(@Param("empresaId") Long empresaId);

    List<Queja> findByEmpresaIdAndIsVisibleTrue(Long empresaId);
    
    List<Queja> findByIsVisibleTrue();

}
