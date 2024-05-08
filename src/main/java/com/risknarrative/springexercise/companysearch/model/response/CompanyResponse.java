package com.risknarrative.springexercise.companysearch.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.risknarrative.springexercise.companysearch.model.common.AddressResponse;
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
public class CompanyResponse implements Serializable {
    private String company_number;
    private String company_type;
    private String title;
    private String company_status;
    private String date_of_creation;
    private AddressResponse address;
    private List<OfficerResponse> officers;
}
