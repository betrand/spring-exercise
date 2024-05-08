package com.risknarrative.springexercise.companysearch.unittest.service;

import com.risknarrative.springexercise.companysearch.domain.entity.Address;
import com.risknarrative.springexercise.companysearch.domain.repository.AddressRepository;
import com.risknarrative.springexercise.companysearch.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.risknarrative.springexercise.companysearch.util.TestUtil.mockAddress;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressServiceTest {

    @Mock
    AddressRepository addressRepository;

    AddressService addressService;

    public static final String postalCode = "AB1 1XY";

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        addressService = new AddressService(addressRepository);
    }

    @Test
    void saveAddress_whenAddressExists_shouldNotSaveAddress() {

        Address expected = mockAddress(1, postalCode);

        when(addressRepository.findAddressByCriteria(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(expected);

        Address actual = addressService.saveAddress(expected);

        assertNotNull(actual);
        assertSame(expected, actual);
        verify(addressRepository, never()).save(expected);
    }

    @Test
    void whenAddressExists_returnExistingAddress() {

        Address expected = mockAddress(null, postalCode);

        when(addressRepository.findAddressByCriteria(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(expected);

        Address actual = addressService.findAddress(expected);

        assertNotNull(actual);
        assertEquals(expected, actual);

        verify(addressRepository, times(1)).findAddressByCriteria(
                expected.getLocality(),
                expected.getPostal_code(),
                expected.getPremises(),
                expected.getAddress_line_1(),
                expected.getCountry());
    }

    @Test
    void whenAddressDoesNotExist_saveAndReturnNewAddress() {

        Address expected = mockAddress(1, postalCode);

        when(addressRepository.findAddressByCriteria(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(null);

        when(addressRepository.save(any(Address.class)))
                .thenReturn(expected);

        Address result = addressService.saveAddress(expected);

        assertNotNull(result);
        assertEquals(expected, result);

        verify(addressRepository, times(1)).save(expected);
    }
}
