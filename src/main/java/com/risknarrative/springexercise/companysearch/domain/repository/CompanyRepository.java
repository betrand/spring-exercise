package com.risknarrative.springexercise.companysearch.domain.repository;

import com.risknarrative.springexercise.companysearch.domain.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, String> {

    Company findByCompanyNumber(String company_number);

    List<Company> findByTitle(String title);

}
