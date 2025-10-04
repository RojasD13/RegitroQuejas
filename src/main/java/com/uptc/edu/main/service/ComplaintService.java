package com.uptc.edu.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uptc.edu.main.model.Company;
import com.uptc.edu.main.model.Complaint;
import com.uptc.edu.main.repository.ComplaintRepo;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepo complaintRepo;

    public Complaint saveComplaint(Complaint complaint) {
        return complaintRepo.save(complaint);
    }

    public List<Complaint> listComplaints() {
        return complaintRepo.findAll();
    }

    public List<Complaint> getComplaintsByCompany(Company company) {
        return complaintRepo.findByCompany(company);
    }

    public Optional<Complaint> searchById(Long id) {
        return complaintRepo.findById(id);
    }

    public List<Complaint> findByIsVisibleTrue() {
        return complaintRepo.findByIsVisibleTrue();
    }

    public List<Complaint> findByCompanyIdAndIsVisibleTrue(Long companyId) {
        return complaintRepo.findByCompanyIdAndIsVisibleTrue(companyId);
    }
}
