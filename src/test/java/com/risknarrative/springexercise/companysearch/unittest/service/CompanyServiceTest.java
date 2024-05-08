package com.risknarrative.springexercise.companysearch.unittest.service;

import com.risknarrative.springexercise.companysearch.domain.entity.Address;
import com.risknarrative.springexercise.companysearch.domain.entity.Company;
import com.risknarrative.springexercise.companysearch.domain.repository.CompanyRepository;
import static com.risknarrative.springexercise.companysearch.util.TestUtil.*;

import com.risknarrative.springexercise.companysearch.service.AddressService;
import com.risknarrative.springexercise.companysearch.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private AddressService addressService;

    private CompanyService companyService;

    public static final String companyNumber = "06500244";
    public static final String companyName = "BBC LIMITED";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        companyService = new CompanyService(companyRepository, addressService);
    }

    @Test
    void saveCompanies_whenNewCompanies_shouldSaveAndReturnCompanies() {

        Company company = mockCompany(companyNumber, companyName);
        Address address = company.getAddress();

        final List<Company> companies = Arrays.asList(company);

        when(companyRepository.save(any(Company.class)))
                .thenReturn(company);

        when(addressService.saveAddress(any(Address.class)))
                .thenReturn(address);

        final List<Company> result = companyService.saveCompanies(companies);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(companies, result);
        verify(companyRepository, times(1)).save(company);
    }

    @Test
    void findByCompanyName_whenCalled_shouldReturnCompanies() {

        final Company company = mockCompany(companyNumber, companyName);
        final List<Company> companies = Arrays.asList(company);

        when(companyRepository.findByTitle(companyName))
                .thenReturn(companies);

        final List<Company> results = companyService.findByCompanyName(companyName);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(companies, results);
        verify(companyRepository).findByTitle(companyName);
    }

    @Test
    void findByCompanyNumber_whenExists_shouldReturnCompany() {

        final Company company = mockCompany(companyNumber, companyName);

        when(companyRepository.findByCompanyNumber(companyNumber))
                .thenReturn(company);

        Company result = companyService.findByCompanyNumber(companyNumber);

        assertNotNull(result);
        assertEquals(company, result);
        assertEquals(company.getTitle(), result.getTitle());
        verify(companyRepository).findByCompanyNumber(companyNumber);
    }

    @Test
    void saveCompany_whenCompanyDoesNotExist_shouldSaveCompany() {

        Company company = mockCompany(companyNumber, companyName);
        Address address = company.getAddress();

        when(companyRepository.findByCompanyNumber(company.getCompany_number()))
                .thenReturn(null);

        when(addressService.saveAddress(any(Address.class)))
                .thenReturn(address);

        when(companyRepository.save(any(Company.class)))
                .thenReturn(company);

        Company result = companyService.saveCompany(company);

        assertNotNull(result);
        assertEquals(company, result);
        assertEquals(company.getTitle(), result.getTitle());
        verify(companyRepository).save(company);
        verify(addressService).saveAddress(company.getAddress());
    }

    @Test
    void saveCompany_whenCompanyExists_shouldNotSaveCompany() {

        Company company = mockCompany(companyNumber, companyName);

        when(companyRepository.findByCompanyNumber(company.getCompany_number()))
                .thenReturn(company);

        Company result = companyService.saveCompany(company);

        assertSame(company, result);
        verify(companyRepository, never()).save(company);
    }
}