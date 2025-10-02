package com.uptc.edu.main.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uptc.edu.main.dto.CompanySummaryDTO;
import com.uptc.edu.main.model.Company; 
@Repository 
public interface CompanyRepo extends JpaRepository<Company, Long> {
    List<Company> findAllByOrderByNameAsc();

    boolean existsByName(String companyName);

    @Query("SELECT COUNT(q) FROM Company c JOIN c.complaints q WHERE c.id = :companyId")
    Long countComplaintsByCompanyId(@Param("companyId") Long companyId);

    Optional<Company> findByName(String companyName);
    
    @Query("SELECT new com.uptc.edu.main.dto.CompanySummaryDTO(c.id, c.name, " +
           "COALESCE(COUNT(q.id), 0)) " +
           "FROM Company c " +
           "LEFT JOIN c.complaints q ON q.isVisible = true " +
           "GROUP BY c.id, c.name " +
           "ORDER BY c.name ASC")
    List<CompanySummaryDTO> findCompanySummaryWithComplaintCount();
}

