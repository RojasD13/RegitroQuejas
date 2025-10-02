package com.uptc.edu.main.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uptc.edu.main.dto.CompanySummaryDTO;
import com.uptc.edu.main.model.Company;
import com.uptc.edu.main.repository.CompanyRepo;
import com.uptc.edu.main.repository.ComplaintRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CompanyService { 
    private final CompanyRepo companyRepo;
    private final ComplaintRepo complaintRepo;

    public List<Company> listCompanies() {
        log.debug("Listing all companies");
        return companyRepo.findAll();
    }

    public Optional<Company> searchByName(String name) {
        log.debug("Searching company by name: {}", name);
        return companyRepo.findByName(name);
    }

    @Transactional
    public Company save(Company company) {
        log.info("Saving company: {}", company.getName());
        return companyRepo.save(company);
    }

    public Optional<Company> searchById(Long id) {
        log.debug("Searching company by id: {}", id);
        return companyRepo.findById(id);
    }
    /**
     @return List of company summaries ordered by name
     */
    public List<CompanySummaryDTO> getCompanySummaryWithComplaintCount() {
        log.debug("Fetching company summary with complaint counts");
        List<CompanySummaryDTO> summary = companyRepo.findCompanySummaryWithComplaintCount();
        log.info("Retrieved {} companies with complaint counts", summary.size());
        return summary;
    }

    private CompanySummaryDTO mapToSummaryDTO(Company company) {
        CompanySummaryDTO dto = new CompanySummaryDTO();
        dto.setId(company.getId());
        dto.setCompanyName(company.getName());
        Long complaintCount = complaintRepo.countByCompanyIdAndIsVisibleTrue(company.getId());
        dto.setTotalComplaints(complaintCount);        
        return dto;
    }
}