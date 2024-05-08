package com.risknarrative.springexercise.companysearch.model.mapper;

import static io.micrometer.common.util.StringUtils.isBlank;
import com.risknarrative.springexercise.companysearch.domain.entity.Address;
import com.risknarrative.springexercise.companysearch.domain.entity.Company;
import com.risknarrative.springexercise.companysearch.domain.entity.Officer;
import com.risknarrative.springexercise.companysearch.model.common.AddressResponse;
import com.risknarrative.springexercise.companysearch.truproxyapi.response.TruProxyApiCompany;
import com.risknarrative.springexercise.companysearch.truproxyapi.response.TruProxyApiOfficer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.risknarrative.springexercise.companysearch.util.DateUtil.convertToJavaDate;

@Component
public class EntityMapper {

    private EntityMapper() {}

    public static List<Officer> mapOfficerAndAddressToEntities(List<TruProxyApiOfficer> apiOfficers) {
        if (apiOfficers == null || apiOfficers.isEmpty()) {
            return Collections.emptyList();
        }

        final List<Officer> appOfficers = new ArrayList<>();

        apiOfficers.stream()
                .filter((var item) -> isBlank(item.getResigned_on()))
                .forEachOrdered(item -> appOfficers.add(mapOfficerAndAddressToEntity(item)));

        return appOfficers;
    }

    public static Officer mapOfficerAndAddressToEntity(TruProxyApiOfficer item) {
        return Officer.builder()
                .name(item.getName())
                .officer_role(item.getOfficer_role())
                .appointed_on(convertToJavaDate(item.getAppointed_on()))
                .resigned_on(convertToJavaDate(item.getResigned_on()))
                .address(mapAddressToEntity(item.getAddress()))
                .build();
    }

    public static List<Company> mapCompanyAndAddressToEntities(List<TruProxyApiCompany> apiCompanies) {
        if (apiCompanies == null || apiCompanies.isEmpty()) {
            return Collections.emptyList();
        }

        final List<Company> appCompanies = new ArrayList<>();

        apiCompanies.forEach(item -> appCompanies.add(mapCompanyAndAddressToEntity(item)));

        return appCompanies;
    }

    public static Company mapCompanyAndAddressToEntity(TruProxyApiCompany item) {
        return Company.builder()
                .company_number(item.getCompany_number())
                .company_type(item.getCompany_type())
                .title(item.getTitle())
                .company_status(item.getCompany_status())
                .date_of_creation(convertToJavaDate(item.getDate_of_creation()))
                .address(mapAddressToEntity(item.getAddress()))
                .officers(Collections.emptyList())
                .build();
    }

    public static Address mapAddressToEntity(AddressResponse response) {
        return Address.builder()
                .locality(response.getLocality())
                .postal_code(response.getPostal_code())
                .premises(response.getPremises())
                .address_line_1(response.getAddress_line_1())
                .country(response.getCountry())
                .build();
    }
}
