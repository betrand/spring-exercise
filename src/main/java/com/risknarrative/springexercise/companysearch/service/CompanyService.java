package com.risknarrative.springexercise.companysearch.service;

import com.risknarrative.springexercise.companysearch.domain.entity.Address;
import com.risknarrative.springexercise.companysearch.domain.entity.Company;
import com.risknarrative.springexercise.companysearch.domain.repository.CompanyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final AddressService addressService;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, AddressService addressService) {
        this.companyRepository = companyRepository;
        this.addressService = addressService;
    }

    public List<Company> saveCompanies(List<Company> companies) {

        final List<Company> savedCompanies = new ArrayList<>();

        companies.stream()
                .map(this::saveCompany)
                .forEachOrdered(savedCompanies::add);

        return savedCompanies;
    }

    public List<Company> findByCompanyName(String companyName) {
        return companyRepository.findByTitle(companyName);
    }

    public Company findByCompanyNumber(String companyNumber) {
        return companyRepository.findByCompanyNumber(companyNumber);
    }

    public Company saveCompany(Company company) {

        final Company existingCompany = findByCompanyNumber(company.getCompany_number());

        if (existingCompany == null) {
            //save company address to database
            Address address = addressService.saveAddress(company.getAddress());
            company.setAddress(address);
            return companyRepository.save(company);
        }

        return existingCompany;
    }

}
