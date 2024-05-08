package com.risknarrative.springexercise.companysearch.service;

import static com.risknarrative.springexercise.companysearch.util.MessageUtil.*;
import static io.micrometer.common.util.StringUtils.isBlank;
import static io.micrometer.common.util.StringUtils.isNotBlank;
import static com.risknarrative.springexercise.companysearch.model.mapper.EntityMapper.mapCompanyAndAddressToEntities;
import static com.risknarrative.springexercise.companysearch.model.mapper.EntityMapper.mapOfficerAndAddressToEntities;
import static com.risknarrative.springexercise.companysearch.model.mapper.ResponseMapper.mapCompanyToCompanySearchResponse;
import static com.risknarrative.springexercise.companysearch.model.mapper.ResponseMapper.mapCompaniesToCompanySearchResponse;

import com.risknarrative.springexercise.companysearch.domain.entity.Company;
import com.risknarrative.springexercise.companysearch.domain.entity.Officer;
import com.risknarrative.springexercise.companysearch.exception.model.CompanySearchException;
import com.risknarrative.springexercise.companysearch.model.request.CompanySearchRequest;
import com.risknarrative.springexercise.companysearch.model.response.CompanySearchResponse;
import com.risknarrative.springexercise.companysearch.truproxyapi.response.TruProxyApiCompanyResponse;
import com.risknarrative.springexercise.companysearch.truproxyapi.response.TruProxyApiOfficerResponse;
import com.risknarrative.springexercise.companysearch.truproxyapi.util.ApiGateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CompanySearchService {

    private final ApiGateway apiGateway;

    private final CompanyService companyService;

    private final OfficerService officerService;

    @Autowired
    public CompanySearchService(
            ApiGateway apiGateway,
            CompanyService companyService,
            OfficerService officerService) {

        this.apiGateway = apiGateway;
        this.companyService = companyService;
        this.officerService = officerService;
    }

    private static Company apply(Company company) {
        return company;
    }

    public CompanySearchResponse searchCompany(CompanySearchRequest request, String apiKey, String status) {

        if (isBlank(apiKey)) {
            throw new CompanySearchException(X_API_KEY_REQUIRED_MSG, BAD_REQUEST_ERROR_CODE);
        }

        if (isBlank(request.getCompanyNumber()) && isBlank(request.getCompanyName())) {
            throw new CompanySearchException(COMPANY_NUM_OR_NAME_REQUIRED_MSG, BAD_REQUEST_ERROR_CODE);
        }

        if (isNotBlank(request.getCompanyName()) && isNotBlank(request.getCompanyNumber())) {

            return searchApiWithNumber(request, apiKey, status);

        } else if (isBlank(request.getCompanyName()) && isNotBlank(request.getCompanyNumber())) {

            return searchDbWithNumber(request, status);

        } else {

            return searchApiWithName(request, apiKey, status);
        }
    }

    private CompanySearchResponse searchApiWithNumber(CompanySearchRequest request, String apiKey, String status) {
        List<Company> companies = searchCompaniesByQueryParam(request.getCompanyNumber(), apiKey);
        if (companies != null) {
            addOfficersToCompanies(apiKey, companies);
        } else {
        throw new CompanySearchException(COMPANY_NOT_FOUND_MSG, NOT_FOUND_ERROR_CODE);
    }

        //retrieve Company and build company, address and officer response
        final Company company = findCompanyByCompanyNumber(request.getCompanyNumber(), companies);

        if (isNotBlank(status) && status.equalsIgnoreCase(DEFAULT_STATUS)) {
            return mapCompanyToCompanySearchResponse(company);
        } else {
            if (company.getCompany_status().equalsIgnoreCase(status)) {
                return mapCompanyToCompanySearchResponse(company);
            } else {
                throw new CompanySearchException(COMPANY_NOT_FOUND_MSG, NOT_FOUND_ERROR_CODE); // throw 404 error
            }
        }
    }

    private CompanySearchResponse searchDbWithNumber(CompanySearchRequest request, String status) {

        // look in db and retrieve Officers and their Company and build responses
        List<Officer> officers = officerService.findByCompanyNumber(request.getCompanyNumber());

        if (officers != null && !officers.isEmpty()) {
            Company company = officers.get(0).getCompany();
            company.setOfficers(officers);

            return filterCompanyByStatus(company, status);
        } else {
            Company company = companyService.findByCompanyNumber(request.getCompanyNumber());
            if (company != null) {
                return filterCompanyByStatus(company, status);
            }
        }

        throw new CompanySearchException(COMPANY_NOT_FOUND_MSG, NOT_FOUND_ERROR_CODE); // throw 404 error
    }

    private CompanySearchResponse filterCompanyByStatus(Company company, String status) {
        if (status.equalsIgnoreCase(DEFAULT_STATUS)) {
            return mapCompanyToCompanySearchResponse(company);
        } else {
            if (company.getCompany_status().equalsIgnoreCase(status)) {
                return mapCompanyToCompanySearchResponse(company);
            } else {
                throw new CompanySearchException(COMPANY_NOT_FOUND_MSG, NOT_FOUND_ERROR_CODE); // throw 404 error
            }
        }
    }

    private CompanySearchResponse searchApiWithName(CompanySearchRequest request, String apiKey, String status) {
        List<Company> companies = searchCompaniesByQueryParam(request.getCompanyName(), apiKey);
        if (companies != null) {
            addOfficersToCompanies(apiKey, companies);
            return mapCompaniesToCompanySearchResponse(companies, status);
        } else {
            throw new CompanySearchException(COMPANY_NOT_FOUND_MSG, NOT_FOUND_ERROR_CODE);
        }
    }

    private List<Company> addOfficersToCompanies(String apiKey, List<Company> companies) {
        companies.stream().map(CompanySearchService::apply).forEachOrdered(company -> {
            TruProxyApiOfficerResponse apiOfficers = apiGateway.searchOfficerByCompanyNumber(company.getCompany_number(), apiKey);

            if (apiOfficers != null && apiOfficers.getItems() != null && !apiOfficers.getItems().isEmpty()) {
                List<Officer> officers = mapOfficerAndAddressToEntities(apiOfficers.getItems());
                company.setOfficers(officers);

                //Bonus: save officers to db
                saveOfficers(company, officers);
            }
        });
        return companies;
    }

    private void saveOfficers(Company company, List<Officer> officers) {
        if (officers != null) {
            officerService.saveOfficers(company, officers);
        }
    }

    private List<Company> searchCompaniesByQueryParam(String searchTerm, String apiKey) {

        TruProxyApiCompanyResponse apiCompanies = apiGateway.searchCompanyByQueryParam(searchTerm, apiKey);

        if (apiCompanies == null || apiCompanies.getItems() == null || apiCompanies.getItems().isEmpty()) {
            throw new CompanySearchException(COMPANY_NOT_FOUND_MSG, NOT_FOUND_ERROR_CODE);
        }

        List<Company> companies = mapCompanyAndAddressToEntities(apiCompanies.getItems());

        //Bonus: save companies to db
        saveCompanies(companies);

        return companies;
    }

    private List<Company> saveCompanies(List<Company> companies) {
        if (companies != null) {
            return companyService.saveCompanies(companies);
        } else {
            return new ArrayList<>();
        }
    }

    private Company findCompanyByCompanyNumber(String companyNumber, List<Company> companies) {
        for (var company : companies) {
            if (company.getCompany_number().equals(companyNumber)) {
                return company;
            }
        }
        throw new CompanySearchException(COMPANY_NOT_FOUND_MSG, NOT_FOUND_ERROR_CODE); // throw 404 error
    }

}
