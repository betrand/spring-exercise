package com.risknarrative.springexercise.companysearch.unittest.service;

import com.risknarrative.springexercise.companysearch.domain.entity.Address;
import com.risknarrative.springexercise.companysearch.domain.entity.Company;
import com.risknarrative.springexercise.companysearch.domain.entity.Officer;
import com.risknarrative.springexercise.companysearch.domain.repository.OfficerRepository;
import com.risknarrative.springexercise.companysearch.service.AddressService;
import com.risknarrative.springexercise.companysearch.service.OfficerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static com.risknarrative.springexercise.companysearch.util.TestUtil.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class OfficerServiceTest {

    @Mock
    private OfficerRepository officerRepository;

    @Mock
    private AddressService addressService;

    private OfficerService officerService;

    public static final String postalCode = "AB1 1XY";
    public static final String companyNumber = "06500244";
    public static final String companyName = "BBC LIMITED";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        officerService = new OfficerService(officerRepository, addressService);
    }

    @Test
    void saveOfficers_whenNewOfficers_shouldSaveAndReturnOfficers() {

        final Address address = mockAddress(1, postalCode);
        final Officer officer = mockOfficer();
        final Company company = mockCompany(companyNumber, companyName);

        final List<Officer> officers = mockOfficerList();

        when(officerRepository.save(any(Officer.class)))
                .thenReturn(officer);

        when(addressService.saveAddress(any(Address.class)))
                .thenReturn(address);

        final List<Officer> result = officerService.saveOfficers(company, officers);

        assertNotNull(result);
        assertEquals(officers, result);
        assertEquals(1, result.size());

        verify(officerRepository, times(1)).save(officer);
    }

    @Test
    void findByCompanyNumber_whenCalled_shouldReturnOfficers() {

        final List<Officer> officers = mockOfficerList();

        when(officerRepository.findByCompanyNumber(companyNumber))
                .thenReturn(officers);

        final List<Officer> result = officerService.findByCompanyNumber(companyNumber);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(officers, result);
        verify(officerRepository).findByCompanyNumber(companyNumber);
    }

    @Test
    void saveOfficer_whenOfficerExists_shouldNotSaveOfficer() {

        Officer officer = mockOfficer();

        when(officerRepository.findOfficerByCriteria(anyString(), anyString(), anyString()))
                .thenReturn(officer);

        Officer result = officerService.saveOfficer(officer);

        assertSame(officer, result);
        verify(officerRepository, never()).save(officer);
    }

    @Test
    void saveOfficer_whenOfficerDoesNotExist_shouldSaveOfficer() {

        final Address address = mockAddress(1, postalCode);
        final Officer officer = mockOfficer();

        when(officerRepository.findOfficerByCriteria(anyString(), anyString(), anyString()))
                .thenReturn(null);

        when(addressService.saveAddress(any(Address.class)))
                .thenReturn(address);

        when(officerRepository.save(any(Officer.class)))
                .thenReturn(officer);

        Officer result = officerService.saveOfficer(officer);

        assertNotNull(result);
        assertSame(officer, result);
        verify(officerRepository).save(officer);
        verify(addressService).saveAddress(officer.getAddress());
    }
}
