package com.risknarrative.springexercise.companysearch.model.mapper;

import static com.risknarrative.springexercise.companysearch.util.MessageUtil.DEFAULT_STATUS;
import static io.micrometer.common.util.StringUtils.isNotBlank;
import com.risknarrative.springexercise.companysearch.domain.entity.Address;
import com.risknarrative.springexercise.companysearch.domain.entity.Company;
import com.risknarrative.springexercise.companysearch.domain.entity.Officer;
import com.risknarrative.springexercise.companysearch.model.common.AddressResponse;
import com.risknarrative.springexercise.companysearch.model.response.CompanyResponse;
import com.risknarrative.springexercise.companysearch.model.response.CompanySearchResponse;
import com.risknarrative.springexercise.companysearch.model.response.OfficerResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.risknarrative.springexercise.companysearch.util.DateUtil.convertToStringDate;

@Component
public class ResponseMapper {

    private ResponseMapper() {}

    public static CompanySearchResponse mapCompanyToCompanySearchResponse(Company company, List<Officer> officers) {
        return CompanySearchResponse.builder()
                .items(mapCompanyToResponse(company, officers))
                .build()
                .totalResult();
    }

    private static List<CompanyResponse> mapCompanyToResponse(Company company, List<Officer> officers) {
        return List.of(
                CompanyResponse.builder()
                        .company_number(company.getCompany_number())
                        .company_type(company.getCompany_type())
                        .title(company.getTitle())
                        .company_status(company.getCompany_status())
                        .date_of_creation(convertToStringDate(company.getDate_of_creation()))
                        .address(mapAddressToResponse(company.getAddress()))
                        .officers(mapOfficersToResponses(officers))
                        .build()
        );
    }

    public static CompanySearchResponse mapCompanyToCompanySearchResponse(Company company) {
        return CompanySearchResponse.builder()
                .items(mapCompanyToResponse(company))
                .build()
                .totalResult();
    }

    private static List<CompanyResponse> mapCompanyToResponse(Company company) {
        return List.of(
                    CompanyResponse.builder()
                            .company_number(company.getCompany_number())
                            .company_type(company.getCompany_type())
                            .title(company.getTitle())
                            .company_status(company.getCompany_status())
                            .date_of_creation(convertToStringDate(company.getDate_of_creation()))
                            .address(mapAddressToResponse(company.getAddress()))
                            .officers(mapOfficersToResponses(company.getOfficers()))
                            .build()
        );
    }

    public static CompanySearchResponse mapCompaniesToCompanySearchResponse(List<Company> companies, String status) {
        return CompanySearchResponse.builder()
                .items(mapCompaniesToResponse(companies, status))
                .build()
                .totalResult();
    }

    private static List<CompanyResponse> mapCompaniesToResponse(List<Company> companies, String status) {

        final List<CompanyResponse> companyResponses = new ArrayList<>();

        companies.forEach(company -> {
            if (isNotBlank(status) && status.equalsIgnoreCase(DEFAULT_STATUS)) {
                companyResponses.add(mapCompanyToResponses(company));
            } else {
                if (company.getCompany_status().equalsIgnoreCase(status)) {
                    companyResponses.add(mapCompanyToResponses(company));
                }
            }
        });

        return companyResponses;
    }

    private static CompanyResponse mapCompanyToResponses(Company company) {
        return CompanyResponse.builder()
                .company_number(company.getCompany_number())
                .company_type(company.getCompany_type())
                .title(company.getTitle())
                .company_status(company.getCompany_status())
                .date_of_creation(convertToStringDate(company.getDate_of_creation()))
                .address(mapAddressToResponse(company.getAddress()))
                .officers(mapOfficersToResponses(company.getOfficers()))
                .build();
    }

    public static AddressResponse mapAddressToResponse(Address address) {
        return AddressResponse.builder()
                .locality(address.getLocality())
                .postal_code(address.getPostal_code())
                .premises(address.getPremises())
                .address_line_1(address.getAddress_line_1())
                .country(address.getCountry())
                .build();
    }

    private static List<OfficerResponse> mapOfficersToResponses(List<Officer> officers) {
        if (officers == null || officers.isEmpty()) {
            return Collections.emptyList();
        }

        final List<OfficerResponse> officerResponses = new ArrayList<>();

        officers.stream()
                .filter(officer -> (officer.getResigned_on() == null))
                .forEachOrdered(officer -> officerResponses.add(mapOfficerToResponse(officer)));
        
        return officerResponses;
    }

    private static OfficerResponse mapOfficerToResponse(Officer officer) {
        return OfficerResponse.builder()
                .name(officer.getName())
                .officer_role(officer.getOfficer_role())
                .appointed_on(convertToStringDate(officer.getAppointed_on()))
                .address(mapAddressToResponse(officer.getAddress()))
                .build();
    }
}
