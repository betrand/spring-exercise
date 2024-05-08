package com.risknarrative.springexercise.companysearch.ittest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.risknarrative.springexercise.companysearch.controller.CompanySearchController;
import com.risknarrative.springexercise.companysearch.exception.model.CompanySearchException;
import com.risknarrative.springexercise.companysearch.model.request.CompanySearchRequest;
import com.risknarrative.springexercise.companysearch.model.response.CompanySearchResponse;
import com.risknarrative.springexercise.companysearch.service.CompanySearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.risknarrative.springexercise.companysearch.util.FileUtil.readFile;
import static com.risknarrative.springexercise.companysearch.util.MessageUtil.*;
import static com.risknarrative.springexercise.companysearch.util.TestUtil.expectedResponse;
import static com.risknarrative.springexercise.companysearch.util.TestUtil.searchRequest;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

// We need these for Wiremock test but Wiremock is not yet supported for Java17
//import com.github.tomakehurst.wiremock.WireMockServer;
//import com.github.tomakehurst.wiremock.client.WireMock;
//import static com.github.tomakehurst.wiremock.client.WireMock.*;

@ActiveProfiles("test")
@WebMvcTest(CompanySearchController.class)
class CompanySearchControllerItTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanySearchService companySearchService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_KEY = "ABC_123XyZ";

    private static final String BASE_FILE_PATH = "src/test/resources/json/";
    
    private static final String searchRequest = "search-request.json";
    
    private static final String searchResponse = "success-response.json";
    
    private static final String activeStatus = "active";
    
    private static final String apiPath = "/api/v1/company";
    
    private static final String emptyString = "";
    
    private static final String noneStatus = "none";

//    private WireMockServer wireMockServer;

    @BeforeEach
    void setup() {
//        wireMockServer = new WireMockServer();
//        configureFor("localhost", 9080);
//          wireMockServer.start();
    }

    @Test
    void searchCompany_whenValidRequest_shouldReturnSuccess() throws Exception {

        // given
        CompanySearchRequest request = (CompanySearchRequest) readFile(BASE_FILE_PATH + searchRequest, CompanySearchRequest.class);

        CompanySearchResponse expectedResponse = (CompanySearchResponse) readFile(BASE_FILE_PATH + searchResponse, CompanySearchResponse.class);

        when(companySearchService.searchCompany(any(), anyString(), anyString()))
                .thenReturn(expectedResponse);

        MediaType jsonMediaType = MediaType.APPLICATION_JSON;

        mockMvc.perform(post(apiPath)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header(X_API_KEY, API_KEY)
                        .param("company_status", activeStatus))

                .andExpect(status().isOk())
                .andExpect(content().contentType(jsonMediaType))

                .andExpect(content().string(objectMapper.writeValueAsString(expectedResponse)))
                .andExpect(jsonPath("$.total_results", is(1)))
                .andExpect(jsonPath("$.items", hasSize(1))
                // Validate more fields as necessary
                );

        verify(companySearchService).searchCompany(request,
                API_KEY,
                activeStatus);


    }

    @Test
    void searchCompany_whenValidNoStatus_shouldReturnSuccess() throws Exception {

        // given
        CompanySearchRequest request = (CompanySearchRequest) readFile(BASE_FILE_PATH + searchRequest, CompanySearchRequest.class);

        CompanySearchResponse expectedResponse = (CompanySearchResponse) readFile(BASE_FILE_PATH + searchResponse, CompanySearchResponse.class);

        when(companySearchService.searchCompany(request, API_KEY, noneStatus))
                .thenReturn(expectedResponse);

        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(X_API_KEY, API_KEY)
                        // company_status param not provided
                        .param("company_status", emptyString))

                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(expectedResponse)))
                .andExpect(jsonPath("$.total_results", is(1)))
                .andExpect(jsonPath("$.items", hasSize(1))
                );

        verify(companySearchService).searchCompany(request,
                API_KEY,
                noneStatus);


    }

    @Test
    void searchCompany_whenStatusParamIsMissing_thenReturnsSuccess() throws Exception {
        CompanySearchRequest searchRequest = searchRequest("BBC LIMITED", "06500244");
        CompanySearchResponse response = expectedResponse();

        // Assuming 'none' is the DEFAULT_STATUS
        when(companySearchService.searchCompany(any(CompanySearchRequest.class), anyString(), eq(noneStatus)))
                .thenReturn(response);

        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest))
                .header(X_API_KEY, API_KEY))
                .andExpect(status().isOk());
    }

    @Test
    void searchCompany_whenRequestIsNotProvided_thenBadRequest() throws Exception {
        // No Request Payload provided
        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_API_KEY, API_KEY))
                // Empty content
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchCompany_whenNoRecordFound_thenNotFound() throws Exception {
        CompanySearchException exception = new CompanySearchException("Not found", 404);
        when(companySearchService.searchCompany(any(), anyString(), anyString()))
                .thenThrow(exception);

        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        // Passing request Json hardcoded
                        .content("{ \"companyNumber\": \"06500245\" }")
                        .header(X_API_KEY, API_KEY))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchCompany_whenApiKeyIsMissing_thenBadRequest() throws Exception {
        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"companyName\" : \"BBC LIMITED\",\n" +
                                "    \"companyNumber\" : \"06500244\"\n" +
                                "}"))
                // No API key provided
                .andExpect(status().isBadRequest());
    }

}

