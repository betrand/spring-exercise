package com.risknarrative.springexercise.companysearch.util;

import com.risknarrative.springexercise.companysearch.domain.entity.Address;
import com.risknarrative.springexercise.companysearch.domain.entity.Company;
import com.risknarrative.springexercise.companysearch.domain.entity.Officer;
import com.risknarrative.springexercise.companysearch.model.common.AddressResponse;
import com.risknarrative.springexercise.companysearch.model.request.CompanySearchRequest;
import com.risknarrative.springexercise.companysearch.model.response.CompanyResponse;
import com.risknarrative.springexercise.companysearch.model.response.CompanySearchResponse;
import com.risknarrative.springexercise.companysearch.model.response.OfficerResponse;

import java.util.List;

import static com.risknarrative.springexercise.companysearch.util.DateUtil.convertToJavaDate;
import static com.risknarrative.springexercise.companysearch.util.DateUtil.convertToStringDate;
import static java.util.Arrays.asList;

public final class TestUtil {

    public static CompanySearchRequest searchRequest(String companyName, String companyNumber) {
        return new CompanySearchRequest(companyName, companyNumber);
    }

    public static CompanySearchResponse expectedResponse() {
        return CompanySearchResponse.builder()
                .items(asList(mockCompanyResponse()))
                .build()
                .totalResult();
    }

    private static CompanyResponse mockCompanyResponse() {
        return new CompanyResponse(
                "company_number",
                "company_type",
                "title",
                "company_status",
                "date_of_creation",
                mockAddressResponse(),
                asList(mockOfficerResponse())
        );
    }

    private static OfficerResponse mockOfficerResponse() {
        return new OfficerResponse(
                "name",
                "officer_role",
                "appointed_on",
                mockAddressResponse()
        );
    }

    public static AddressResponse mockAddressResponse() {
        return new AddressResponse(
                "locality",
                "postal_code",
                "premises",
                "address_line_1",
                "country"
        );
    }

    public static Address mockAddress(Integer id, String postalCode) {
        Address address = new Address();
        address.setId(id);
        address.setPostal_code(postalCode);
        address.setLocality("Retford");
        address.setPremises("Boswell Cottage Main Street");
        address.setAddress_line_1("North Leverton");
        address.setCountry("England");
        return address;
    }

    public static CompanySearchResponse mockCompanySearchResponse(String companyNumber, String companyName) {
        return CompanySearchResponse.builder()
                .items(asList(mockCompanyResponse(companyNumber, companyName)))
                .build()
                .totalResult();
    }

    private static CompanyResponse mockCompanyResponse(String companyNumber, String companyName) {
        Company company = mockCompany(companyNumber, companyName);
        return new CompanyResponse(
                companyNumber,
                company.getCompany_type(),
                companyName,
                company.getCompany_status(),
                convertToStringDate(company.getDate_of_creation()),
                mockAddressResponse(),
                asList(mockOfficerResponse())
        );
    }

    public static Company mockCompany(String companyNumber, String companyName) {

        Address address = mockAddress(1, "DN22 0AD");

        Company company = new Company();
        company.setCompany_number(companyNumber);
        company.setTitle(companyName);
        company.setCompany_type("ltd");
        company.setCompany_status("active");
        company.setDate_of_creation(convertToJavaDate("2024-05-07"));
        company.setAddress(address);

        return company;
    }

    public static List<Company> mockCompanyList() {
        return asList(mockCompany("06500244", "BBC LIMITED"));
    }

    public static Officer mockOfficer() {

        Address address = mockAddress(1, "AB1 1XY");
        Company company = mockCompany("06500244", "BBC LIMITED");

        Officer officer = new Officer();
        officer.setName("BOXALL, Sarah Victoria");
        officer.setOfficer_role("Secretary");
        officer.setAppointed_on(convertToJavaDate("2008-02-11"));

        officer.setCompany(company);
        officer.setAddress(address);

        return officer;
    }

    public static List<Officer> mockOfficerList() {
        return asList(mockOfficer());
    }

}
