package com.risknarrative.springexercise.companysearch.service;

import com.risknarrative.springexercise.companysearch.domain.entity.Address;
import com.risknarrative.springexercise.companysearch.domain.entity.Company;
import com.risknarrative.springexercise.companysearch.domain.entity.Officer;
import com.risknarrative.springexercise.companysearch.domain.repository.OfficerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OfficerService {

    private final OfficerRepository officerRepository;

    @Autowired
    private final AddressService addressService;

    @Autowired
    public OfficerService(OfficerRepository officerRepository, AddressService addressService) {
        this.officerRepository = officerRepository;
        this.addressService = addressService;
    }

    public List<Officer> saveOfficers(Company company, List<Officer> officers) {

        officers.stream()
                .map(officer -> {
                    officer.setCompany(company);
                    return officer;
                }).forEachOrdered(this::saveOfficer);

        return officers;
    }

    public List<Officer> findByCompanyNumber(String companyNumber) {
        return officerRepository.findByCompanyNumber(companyNumber);
    }

    public Officer saveOfficer(Officer officer) {

        final Officer existingOfficer = findOfficer(officer);

        if (existingOfficer == null) {
            //save officer address to database
            Address address = addressService.saveAddress(officer.getAddress());
            officer.setAddress(address);
            return officerRepository.save(officer);
        }

        return existingOfficer;
    }

    /**
     * Finds an Officer to see if it is already stored to avoid duplicate
     * storage.
     *
     * @param officer The Officer to use its fields for the find operation.
     * @return An Officer object or null if no officer match is found.
     */
    private Officer findOfficer(Officer officer) {
        return officerRepository.findOfficerByCriteria(
                officer.getCompany().getCompany_number(),
                officer.getName(),
                officer.getOfficer_role());
    }
}
