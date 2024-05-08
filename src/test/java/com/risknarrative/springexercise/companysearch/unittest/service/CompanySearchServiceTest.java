package com.risknarrative.springexercise.companysearch.unittest.service;

import com.risknarrative.springexercise.companysearch.exception.model.CompanySearchException;
import com.risknarrative.springexercise.companysearch.model.request.CompanySearchRequest;
import com.risknarrative.springexercise.companysearch.model.response.CompanySearchResponse;
import com.risknarrative.springexercise.companysearch.service.CompanySearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.risknarrative.springexercise.companysearch.util.MessageUtil.COMPANY_NOT_FOUND_MSG;
import static com.risknarrative.springexercise.companysearch.util.MessageUtil.NOT_FOUND_ERROR_CODE;
import static com.risknarrative.springexercise.companysearch.util.TestUtil.*;
import static com.risknarrative.springexercise.companysearch.util.TruProxyApiUtil.mockTruProxyApiCompanyResponse;
import static com.risknarrative.springexercise.companysearch.util.TruProxyApiUtil.mockTruProxyApiOfficerResponse;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.risknarrative.springexercise.companysearch.domain.entity.Company;
import com.risknarrative.springexercise.companysearch.service.CompanyService;
import com.risknarrative.springexercise.companysearch.service.OfficerService;
import com.risknarrative.springexercise.companysearch.truproxyapi.util.ApiGateway;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CompanySearchServiceTest {

    @Mock
    private ApiGateway apiGateway;

    @Mock
    private CompanyService companyService;

    @Mock
    private OfficerService officerService;

    private CompanySearchService companySearchService;

    private static final String apiKey = "ABC_123XyZ";

    private static final String BASE_FILE_PATH = "src/test/resources/json/";

    public static final String companyNumber = "06500244";

    public static final String companyName = "BBC LIMITED";

    public static final String activeStatus = "active";

    public static final String emptyString = "";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        companySearchService = new CompanySearchService(apiGateway, companyService, officerService);
    }

    @Test
    void searchCompany_whenValidRequestWithNumberAndName_shouldSearchApiWithNumber() {

        //given
        CompanySearchRequest request = new CompanySearchRequest(companyName, companyNumber);
        List<Company> companyList = asList(mockCompany(companyNumber, companyName));
        CompanySearchResponse expectedResponse = mockCompanySearchResponse(companyNumber, companyName);

        when(apiGateway.searchCompanyByQueryParam(anyString(), anyString()))
                .thenReturn(mockTruProxyApiCompanyResponse());

        when(apiGateway.searchOfficerByCompanyNumber(anyString(), anyString()))
                .thenReturn(mockTruProxyApiOfficerResponse());

        when(companyService.saveCompanies(anyList()))
                .thenReturn(companyList);

        when(officerService.saveOfficers(any(), any()))
                .thenReturn(mockOfficerList());

        CompanySearchResponse actualResponse = companySearchService.searchCompany(request, apiKey, activeStatus);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getItems().size(), actualResponse.getItems().size());
        assertEquals(expectedResponse.getTotal_results(), actualResponse.getTotal_results());
        verify(apiGateway).searchCompanyByQueryParam(companyNumber, apiKey);
        verify(companyService).saveCompanies(companyList);
    }

    @Test
    void searchCompany_whenValidRequestWithNumberAndName_shouldSearchApiWithNumber_andNotFound() {

        //given
        CompanySearchRequest request = new CompanySearchRequest(companyName, companyNumber);
        CompanySearchException expectedResponse = new CompanySearchException(COMPANY_NOT_FOUND_MSG, NOT_FOUND_ERROR_CODE);

        when(apiGateway.searchCompanyByQueryParam(anyString(), anyString()))
                .thenReturn(null);

        CompanySearchException actualResponse = assertThrows(CompanySearchException.class, () ->
                companySearchService.searchCompany(request, apiKey, activeStatus)
        );

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
        verify(apiGateway).searchCompanyByQueryParam(companyNumber, apiKey);
    }

    @Test
    void searchCompany_whenValidRequestWithNumberAndNoName_shouldSearchDbWithNumber() {

        //given
        CompanySearchRequest request = new CompanySearchRequest(emptyString, companyNumber);
        CompanySearchResponse expectedResponse = mockCompanySearchResponse(companyNumber, companyName);

        when(officerService.findByCompanyNumber(anyString()))
                .thenReturn(mockOfficerList());

        CompanySearchResponse actualResponse = companySearchService.searchCompany(request, apiKey, activeStatus);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getItems().size(), actualResponse.getItems().size());
        assertEquals(expectedResponse.getTotal_results(), actualResponse.getTotal_results());
        verify(officerService).findByCompanyNumber(companyNumber);
    }
    @Test
    void searchCompany_whenValidRequestWithOnlyNumber_shouldSearchDbWithNumber_andOfficerNotFound() {

        //given
        CompanySearchRequest request = new CompanySearchRequest(emptyString, companyNumber);
        Company company = mockCompany(companyNumber, companyName);
        CompanySearchResponse expectedResponse = mockCompanySearchResponse(companyNumber, companyName);

        when(officerService.findByCompanyNumber(anyString()))
                .thenReturn(new ArrayList<>());

        when(companyService.findByCompanyNumber(anyString()))
                .thenReturn(company);

        CompanySearchResponse actualResponse = companySearchService.searchCompany(request, apiKey, activeStatus);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getItems().size(), actualResponse.getItems().size());
        assertEquals(expectedResponse.getTotal_results(), actualResponse.getTotal_results());
        verify(officerService).findByCompanyNumber(companyNumber);
        verify(companyService).findByCompanyNumber(companyNumber);

    }
    @Test
    void searchCompany_withNumber_shouldSearchDbWithNumber_OfficerNotFound_andCompanyNotFound() {

        //given
        CompanySearchRequest request = new CompanySearchRequest(emptyString, companyNumber);
        CompanySearchException expectedResponse = new CompanySearchException(COMPANY_NOT_FOUND_MSG, NOT_FOUND_ERROR_CODE);

        when(officerService.findByCompanyNumber(anyString()))
                .thenReturn(new ArrayList<>());

        when(companyService.findByCompanyNumber(anyString()))
                .thenReturn(null);

        CompanySearchException actualResponse = assertThrows(CompanySearchException.class, () ->
                companySearchService.searchCompany(request, apiKey, activeStatus)
        );

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
        verify(officerService).findByCompanyNumber(companyNumber);
        verify(companyService).findByCompanyNumber(companyNumber);

    }

    @Test
    void searchCompany_withName_shouldSearchApiWithName_andReturnSuccess() {

        //given
        CompanySearchRequest request = new CompanySearchRequest(companyName, emptyString);
        List<Company> companyList = asList(mockCompany(companyNumber, companyName));
        CompanySearchResponse expectedResponse = mockCompanySearchResponse(companyNumber, companyName);

        when(apiGateway.searchCompanyByQueryParam(anyString(), anyString()))
                .thenReturn(mockTruProxyApiCompanyResponse());

        when(companyService.saveCompanies(anyList()))
                .thenReturn(companyList);

        CompanySearchResponse actualResponse = companySearchService.searchCompany(request, apiKey, activeStatus);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getItems().size(), actualResponse.getItems().size());
        assertEquals(expectedResponse.getTotal_results(), actualResponse.getTotal_results());
        verify(apiGateway).searchCompanyByQueryParam(companyName, apiKey);
        verify(companyService).saveCompanies(companyList);
    }
    @Test
    void searchCompany_withName_shouldSearchApiWithName_andCompanyNotFound() {

        //given
        CompanySearchRequest request = new CompanySearchRequest(companyName, emptyString);
        CompanySearchException expectedResponse = new CompanySearchException(COMPANY_NOT_FOUND_MSG, NOT_FOUND_ERROR_CODE);

        when(apiGateway.searchCompanyByQueryParam(anyString(), anyString()))
                .thenReturn(null);

        CompanySearchException actualResponse = assertThrows(CompanySearchException.class, () ->
                companySearchService.searchCompany(request, apiKey, activeStatus)
        );

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
        verify(apiGateway).searchCompanyByQueryParam(companyName, apiKey);
    }

}
