package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.exception.CustomExceptions;
import com.codecool.eventorganizer.model.Address;
import com.codecool.eventorganizer.model.City;
import com.codecool.eventorganizer.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final CityService cityService;

    @Autowired
    public AddressService(AddressRepository addressRepository, CityService cityService) {
        this.addressRepository = addressRepository;
        this.cityService = cityService;
    }

    // basic CRUD operations

    public Address getAddressById(UUID id) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isPresent()) {
            return optionalAddress.get();
        } else {
            throw new NoSuchElementException("Address not found by given id.");
        }
    }

    // helper methods

    private void checkIfRequiredDataExists(Address address) {
        City city = address.getCity();
        String zipCode = address.getZipCode();
        String street = address.getStreet();
        String house = address.getHouse();
        if (city == null || zipCode == null || street == null || house == null || "".equals(zipCode)
                || "". equals(street) || "".equals(house)) {
            throw new CustomExceptions.MissingAttributeException("Missing one or more attribute(s) in country.");
        }
        City savedCity = cityService.getCityById(city.getId());
        if (!city.equals(savedCity)) {
            throw new IllegalArgumentException("City's data does not match with the one in database.");
        }
    }

    private void checkIfAddressExists(UUID id) {
        if (!addressRepository.existsById(id)) {
            throw new NoSuchElementException("Address not found by given id.");
        }
    }

    // logic

    public ResponseEntity<String> createAddress(Address address) {
        checkIfRequiredDataExists(address);
        if (address.getId() != null) {
            throw new CustomExceptions.IdCanNotExistWhenCreatingEntityException();
        }
        addressRepository.save(address);
        return ResponseEntity.status(HttpStatus.CREATED).body("Address successfully created.");
    }

    public ResponseEntity<String> updateAddress(Address address) {
        checkIfRequiredDataExists(address);
        checkIfAddressExists(address.getId());
        addressRepository.save(address);
        return ResponseEntity.status(HttpStatus.OK).body("Address successfully updated.");
    }

    public ResponseEntity<String> deleteAddress(UUID id) {
        checkIfAddressExists(id);
        addressRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Address successfully deleted.");
    }
}
