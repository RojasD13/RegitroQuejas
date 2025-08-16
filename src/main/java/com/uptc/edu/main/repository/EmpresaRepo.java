package com.uptc.edu.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import com.uptc.edu.main.moldel.Empresa;

public interface EmpresaRepo extends JpaRepository<Empresa, Long> {
    
        
    List<Empresa> findAllByOrderByNombreEmpresaAsc();
    
    boolean existsByNombreEmpresa(String nombreEmpresa);
    
    @Query("SELECT COUNT(q) FROM Empresa e JOIN e.quejas q WHERE e.id = :empresaId")
    Long countQuejasByEmpresaId(@Param("empresaId") Long empresaId);
}
