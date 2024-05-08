package com.risknarrative.springexercise.companysearch.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.risknarrative.springexercise.companysearch.model.common.AddressResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfficerResponse implements Serializable {
        private String name;
        private String officer_role;
        private String appointed_on;
        private AddressResponse address;
}
