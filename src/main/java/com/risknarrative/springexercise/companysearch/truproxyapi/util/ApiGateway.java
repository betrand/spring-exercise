package com.risknarrative.springexercise.companysearch.truproxyapi.util;

import com.risknarrative.springexercise.companysearch.truproxyapi.response.TruProxyApiCompanyResponse;
import com.risknarrative.springexercise.companysearch.truproxyapi.response.TruProxyApiOfficerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import static com.risknarrative.springexercise.companysearch.truproxyapi.util.HttpEntityUtil.httpEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApiGateway {

    @Value("${tru_proxy_api.base.url}")
    private String baseUrl;
    private final RestTemplate restTemplate;

    private static final String SEARCH_BY_QUERY = "Search?Query=";
    private static final String SEARCH_BY_OFFICER_COMPANY_NUMBER = "Officers?CompanyNumber=";

    @Autowired
    public ApiGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public TruProxyApiCompanyResponse searchCompanyByQueryParam(String searchTerm, String apiKey) {

        String url = baseUrl + SEARCH_BY_QUERY + searchTerm;

        ResponseEntity<TruProxyApiCompanyResponse> response = restTemplate
                .exchange(url, HttpMethod.GET, httpEntity(apiKey), TruProxyApiCompanyResponse.class);

        return response.getBody();
    }

    public TruProxyApiOfficerResponse searchOfficerByCompanyNumber(String number, String apiKey) {

        String url = baseUrl + SEARCH_BY_OFFICER_COMPANY_NUMBER + number;

        ResponseEntity<TruProxyApiOfficerResponse> response = restTemplate
                .exchange(url, HttpMethod.GET, httpEntity(apiKey), TruProxyApiOfficerResponse.class);

        return response.getBody();
    }
}
