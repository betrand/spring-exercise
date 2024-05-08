package com.risknarrative.springexercise.companysearch.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Value("${rest_template.connect.timeout.duration}")
    private int connectTimeout;

    @Value("${rest_template.read.timeout.duration}")
    private int readTimeout;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                // Set the TruProxyAPI Timeouts here
                .setConnectTimeout(Duration.ofSeconds(connectTimeout))
                .setReadTimeout(Duration.ofSeconds(readTimeout))
                .build();
    }

    //Add more restTemplateConfig below if needed
}
