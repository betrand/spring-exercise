package com.risknarrative.springexercise.companysearch.domain.repository;

import com.risknarrative.springexercise.companysearch.domain.entity.Officer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OfficerRepository extends JpaRepository<Officer, Integer> {

    @Query(name = "Officer.findByCompanyNumber")
    List<Officer> findByCompanyNumber(String company_number);

    @Query(name = "Officer.findByCriteria")
    Officer findOfficerByCriteria(String company_number, String name, String officer_role);

}
