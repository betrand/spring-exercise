package com.risknarrative.springexercise.companysearch.truproxyapi.response;

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
public class TruProxyApiOfficer implements Serializable {
        private AddressResponse address;
        private String name;
        private String appointed_on;
        private String resigned_on;
        private String officer_role;
        private TruProxyApiOfficerLink links;
        private DateOfBirth date_of_birth;
        private String occupation;
        private String country_of_residence;
        private String nationality;
}
