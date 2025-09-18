package com.uptc.edu.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uptc.edu.main.model.Company;

public interface CompanyRepo extends JpaRepository<Company, Long> {

    List<Company> findAllCompaniesOrderByNameAsc();

    boolean existsByCompanyName(String nombreEmpresa);

    @Query("SELECT COUNT(q) FROM Company c JOIN c.quejas q WHERE c.id = :empresaId")
    Long countComplaintsByCompanyId(@Param("empresaId") Long companyId);

    Optional<Company> findByCompanyName(String companyName);

}
