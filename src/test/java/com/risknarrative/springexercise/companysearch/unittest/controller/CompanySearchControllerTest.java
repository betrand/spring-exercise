package com.risknarrative.springexercise.companysearch.unittest.controller;

import com.risknarrative.springexercise.companysearch.controller.CompanySearchController;
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
import static com.risknarrative.springexercise.companysearch.util.FileUtil.readFile;
import static com.risknarrative.springexercise.companysearch.util.MessageUtil.BAD_REQUEST_ERROR_CODE;
import static com.risknarrative.springexercise.companysearch.util.TestUtil.searchRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanySearchControllerTest {

    @Mock
    CompanySearchService companySearchService;

    CompanySearchController companySearchController;

    private static final String apiKey = "ABC_123XyZ";
    private static final String companyStatus = "active";

    public static final String badRequest = "Bad Request";
    public static final String emptyString = "";

    public static final String successRequestPayload = "search-request.json";
    public static final String successResponsePayload = "success-response.json";

    private static final String BASE_FILE_PATH = "src/test/resources/json/";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        companySearchController = new CompanySearchController(companySearchService);
    }

    @Test
    void searchCompany_whenValidRequest_shouldReturnSuccess() {

        // given
        CompanySearchRequest request = (CompanySearchRequest) readFile(
                BASE_FILE_PATH + successRequestPayload, CompanySearchRequest.class);

        CompanySearchResponse expectedResponse = (CompanySearchResponse) readFile(
                BASE_FILE_PATH + successResponsePayload, CompanySearchResponse.class);

        when(companySearchService.searchCompany(any(), anyString(), anyString()))
                .thenReturn(expectedResponse);

        CompanySearchResponse result = companySearchController.searchCompany(request, apiKey, companyStatus);

        assertNotNull(expectedResponse);
        assertEquals(expectedResponse, result);
        assertEquals(expectedResponse.getTotal_results(), result.getTotal_results());
    }

    @Test
    void searchCompany_whenValidNoStatus_shouldReturnSuccess() {

        // given
        CompanySearchRequest request = (CompanySearchRequest) readFile(
                BASE_FILE_PATH + successRequestPayload, CompanySearchRequest.class);

        CompanySearchResponse expectedResponse = (CompanySearchResponse) readFile(
                BASE_FILE_PATH + successResponsePayload, CompanySearchResponse.class);

        when(companySearchService.searchCompany(any(), anyString(), anyString()))
                .thenReturn(expectedResponse);

        CompanySearchResponse result = companySearchController.searchCompany(request, apiKey, emptyString);

        assertNotNull(expectedResponse);
        assertEquals(expectedResponse, result);
        assertEquals(expectedResponse.getTotal_results(), result.getTotal_results());
    }

    @Test
    void searchCompany_whenNoXApiKey_shouldThrow400() {

        // given
        CompanySearchRequest searchRequest = searchRequest("06500244", "BBC LIMITED");
        CompanySearchException expectedException = new CompanySearchException(badRequest, BAD_REQUEST_ERROR_CODE);

        when(companySearchService.searchCompany(any(), anyString(), anyString()))
                .thenThrow(expectedException);

        CompanySearchException actualException = assertThrows(CompanySearchException.class, () ->
                companySearchController.searchCompany(searchRequest, emptyString, companyStatus)
        );

        assertNotNull(actualException);
        assertEquals(expectedException, actualException);
        assertEquals(expectedException.getMessage(), actualException.getMessage());
    }

    @Test
    void searchCompany_whenInvalidRequest_shouldThrow400() {

        // given
        CompanySearchRequest searchRequest = searchRequest(emptyString, emptyString);
        CompanySearchException expectedException = new CompanySearchException(badRequest, BAD_REQUEST_ERROR_CODE);

        when(companySearchService.searchCompany(any(), anyString(), anyString()))
                .thenThrow(expectedException);

        CompanySearchException actualException = assertThrows(CompanySearchException.class, () ->
                        companySearchController.searchCompany(searchRequest, apiKey, companyStatus)
        );

        assertNotNull(actualException);
        assertEquals(expectedException, actualException);
        assertEquals(expectedException.getMessage(), actualException.getMessage());
    }

    @Test
    void searchCompany_whenNoRecordFound_thenNotFound() {

        // given
        CompanySearchRequest searchRequest = searchRequest(emptyString, "06500245");
        CompanySearchException expectedException = new CompanySearchException("Not Found", 404);

        when(companySearchService.searchCompany(any(), anyString(), anyString()))
                .thenThrow(expectedException);

        CompanySearchException actualException = assertThrows(CompanySearchException.class, () ->
                companySearchController.searchCompany(searchRequest, apiKey, companyStatus)
        );

        assertNotNull(actualException);
        assertEquals(expectedException, actualException);
        assertEquals(expectedException.getMessage(), actualException.getMessage());
    }
}