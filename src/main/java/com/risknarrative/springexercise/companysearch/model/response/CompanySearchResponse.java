package com.risknarrative.springexercise.companysearch.model.response;

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
public class CompanySearchResponse implements Serializable {
    private Integer total_results;
    private List<CompanyResponse> items;

    //Utility method to calculate total_results and avoid saving this class in memory after build
    public CompanySearchResponse totalResult() {
        total_results = items != null ? items.size() : 0;
        return this;
    }
}

