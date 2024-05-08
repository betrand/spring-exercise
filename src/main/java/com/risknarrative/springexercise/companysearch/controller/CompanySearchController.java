package com.risknarrative.springexercise.companysearch.controller;

import com.risknarrative.springexercise.companysearch.model.request.CompanySearchRequest;
import com.risknarrative.springexercise.companysearch.model.response.CompanySearchResponse;
import com.risknarrative.springexercise.companysearch.service.CompanySearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import static com.risknarrative.springexercise.companysearch.util.MessageUtil.DEFAULT_STATUS;
import static com.risknarrative.springexercise.companysearch.util.MessageUtil.X_API_KEY;

@Slf4j
@RestController
@RequestMapping("/api/v1/company")
public class CompanySearchController {

    private final CompanySearchService companySearchService;

    @Autowired
    public CompanySearchController(CompanySearchService companySearchService) {
        this.companySearchService = companySearchService;
    }

    @PostMapping
    public CompanySearchResponse searchCompany(
            @RequestBody CompanySearchRequest request,
            @RequestHeader(X_API_KEY) String apiKey,
            @RequestParam(name = "company_status", defaultValue = DEFAULT_STATUS, required = false) String status) {

        log.info("Executing searchCompany with request: " + request);

        return companySearchService.searchCompany(request, apiKey, status);
    }
}
