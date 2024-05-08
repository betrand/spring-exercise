package com.risknarrative.springexercise.companysearch.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class AddressResponse implements Serializable {
    private String locality;
    private String postal_code;
    private String premises;
    private String address_line_1;
    private String country;
}
