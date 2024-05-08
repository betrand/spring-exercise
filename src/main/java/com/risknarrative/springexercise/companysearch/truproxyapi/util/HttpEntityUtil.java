package com.risknarrative.springexercise.companysearch.truproxyapi.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import static com.risknarrative.springexercise.companysearch.util.MessageUtil.X_API_KEY;

public class HttpEntityUtil {

    private HttpEntityUtil() {}

    public static HttpEntity<String> httpEntity(String apiKey) {

        HttpHeaders headers = new HttpHeaders();

        // Set API key in headers
        headers.set(X_API_KEY, apiKey);

        return new HttpEntity<>(headers);
    }

}
