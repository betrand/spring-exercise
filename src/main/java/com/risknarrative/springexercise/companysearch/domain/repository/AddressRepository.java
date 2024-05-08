package com.risknarrative.springexercise.companysearch.domain.repository;

import com.risknarrative.springexercise.companysearch.domain.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    @Query(name = "Address.findByCriteria")
    Address findAddressByCriteria(String locality, String postal_code, String premises, String address_line_1, String country);

}
