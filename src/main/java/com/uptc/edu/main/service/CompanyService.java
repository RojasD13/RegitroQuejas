package com.uptc.edu.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uptc.edu.main.model.Company;
import com.uptc.edu.main.repository.CompanyRepo;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepo companyRepo;

    public List<Company> listCompanies() {
        return companyRepo.findAll();
    }

    public Optional<Company> searchByName(String name) {
        return companyRepo.findByName(name);
    }

    public Company save(Company company) {
        return companyRepo.save(company);
    }

    public Optional<Company> searchById(Long id) {
        return companyRepo.findById(id);
    }

    public List<Company> findAllByOrderByNameAsc() {
        return companyRepo.findAllByOrderByNameAsc();
    }   

    public List<Company> findAll() {
        return companyRepo.findAll();
    }   
}
