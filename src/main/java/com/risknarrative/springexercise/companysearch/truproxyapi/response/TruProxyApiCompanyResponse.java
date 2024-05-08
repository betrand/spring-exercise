package com.risknarrative.springexercise.companysearch.truproxyapi.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TruProxyApiCompanyResponse implements Serializable {
    private int page_number;
    private String kind;
    private int total_results;
    private List<TruProxyApiCompany> items;
}
