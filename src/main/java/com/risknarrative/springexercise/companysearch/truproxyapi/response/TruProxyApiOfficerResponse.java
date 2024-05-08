package com.risknarrative.springexercise.companysearch.truproxyapi.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class TruProxyApiOfficerResponse implements Serializable {
        private String etag;
        private LinkResponse link;
        private String kind;
        private int items_per_page;
        private List<TruProxyApiOfficer> items;
}
