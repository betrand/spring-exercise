package com.risknarrative.springexercise.companysearch.service;

import com.risknarrative.springexercise.companysearch.domain.entity.Address;
import com.risknarrative.springexercise.companysearch.domain.repository.AddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    /**
     * Finds an Address to see if it is already stored to avoid duplicate
     * storage.
     *
     * @param address The Address to use its fields for the find operation.
     * @return An Address object or null if no address match is found.
     */
    public Address findAddress(Address address) {
        return addressRepository.findAddressByCriteria(
                address.getLocality(),
                address.getPostal_code(),
                address.getPremises(),
                address.getAddress_line_1(),
                address.getCountry());
    }

    public Address saveAddress(Address address) {

        final Address existingAddress = findAddress(address);

        if (existingAddress != null && existingAddress.getId() != null) {
            return existingAddress;
        }

        return addressRepository.save(address);
    }
}
