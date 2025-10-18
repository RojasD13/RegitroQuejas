package com.uptc.edu.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uptc.edu.main.model.Company;
import com.uptc.edu.main.model.Complaint;

public interface ComplaintRepo extends JpaRepository<Complaint, Long> {

    List<Complaint> findByCompany(Company company);

    @Query("SELECT c FROM Complaint c WHERE c.company.id = :companyId")
    List<Complaint> findByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT c FROM Complaint c WHERE c.company.id = :companyId AND c.isVisible = true")
    List<Complaint> findByCompanyIdAndIsVisibleTrue(@Param("companyId") Long companyId);

    @Query("SELECT c FROM Complaint c WHERE c.isVisible = true")
    List<Complaint> findByIsVisibleTrue();
}