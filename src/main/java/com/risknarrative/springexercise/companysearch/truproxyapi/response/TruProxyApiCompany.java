package com.risknarrative.springexercise.companysearch.truproxyapi.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.risknarrative.springexercise.companysearch.model.common.AddressResponse;
import com.risknarrative.springexercise.companysearch.model.common.LinkResponse;
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
public class TruProxyApiCompany implements Serializable {
    private String company_status;
    private String address_snippet;
    private String date_of_creation;
    private Matches matches;
    private String description;
    private LinkResponse link_response;
    private String company_number;
    private String title;
    private String company_type;
    private AddressResponse address;
    private String kind;
    private List<String> description_identifier;
}
