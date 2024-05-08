package com.risknarrative.springexercise.companysearch.util;

import com.risknarrative.springexercise.companysearch.model.common.LinkResponse;
import com.risknarrative.springexercise.companysearch.truproxyapi.response.*;

import java.util.List;

import static com.risknarrative.springexercise.companysearch.util.TestUtil.mockAddressResponse;
import static java.util.Arrays.asList;

public final class TruProxyApiUtil {

    public static TruProxyApiCompanyResponse mockTruProxyApiCompanyResponse() {
        return TruProxyApiCompanyResponse.builder()
                .page_number(1)
                .total_results(1)
                .kind("kind")
                .items(mockTruProxyApiCompanyList())
                .build();
    }

    public static TruProxyApiOfficerResponse mockTruProxyApiOfficerResponse() {
        return TruProxyApiOfficerResponse.builder()
                .etag("etag")
                .link(LinkResponse.builder().self("self").build())
                .kind("kind")
                .items_per_page(1)
                .items(mockTruProxyApiOfficerList())
                .build();
    }

    public static List<TruProxyApiCompany> mockTruProxyApiCompanyList() {
        return asList(mockTruProxyApiCompany("06500244", "BBC LIMITED"));
    }

    public static TruProxyApiCompany mockTruProxyApiCompany(String companyNumber, String companyName) {
        return TruProxyApiCompany.builder()
                .company_status("active")
                .address_snippet("address_snippet")
                .date_of_creation("2024-05-07")
                .matches(Matches.builder().title(asList(1, 3)).build())
                .description("description")
                .link_response(LinkResponse.builder().self("self").build())
                .company_number(companyNumber)
                .title(companyName)
                .company_type("ltd")
                .address(mockAddressResponse())
                .kind("ltd")
                .description_identifier(asList("description_identifier"))
                .build();
    }


    public static List<TruProxyApiOfficer> mockTruProxyApiOfficerList() {
        return asList(mockTruProxyApiOfficer());
    }

    public static TruProxyApiOfficer mockTruProxyApiOfficer() {
        return TruProxyApiOfficer.builder()
                .address(mockAddressResponse())
                .name("BOXALL, Sarah Victoria")
                .appointed_on("2008-02-11")
                .resigned_on("2009-02-11")
                .officer_role("Secretary")
                .links(mockTruProxyApiOfficerLink())
                .date_of_birth(DateOfBirth.builder().month(2).year(2008).build())
                .occupation("occupation")
                .country_of_residence("country_of_residence")
                .nationality("nationality")
                .build();
    }

    private static TruProxyApiOfficerLink mockTruProxyApiOfficerLink() {
        return TruProxyApiOfficerLink.builder()
                .officer(OfficerAppointment.builder().appointments("appointments").build())
                .build();
    }
}
