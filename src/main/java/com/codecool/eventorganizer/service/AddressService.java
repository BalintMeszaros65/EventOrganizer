package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.model.Address;
import com.codecool.eventorganizer.model.City;
import com.codecool.eventorganizer.model.VenueAddress;
import com.codecool.eventorganizer.repository.VenueAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class AddressService {

    private final VenueAddressRepository venueAddressRepository;
    private final CityService cityService;

    @Autowired
    public AddressService(VenueAddressRepository venueAddressRepository, CityService cityService) {
        this.venueAddressRepository = venueAddressRepository;
        this.cityService = cityService;
    }

    // basic CRUD operations

    public VenueAddress getVenueAddressById(UUID id) {
        Optional<VenueAddress> optionalAddress = venueAddressRepository.findById(id);
        if (optionalAddress.isPresent()) {
            return optionalAddress.get();
        } else {
            throw new NoSuchElementException("VenueAddress not found by given id.");
        }
    }

    // helper methods

    private void checkIfCityMatchesInDatabase(Address address) {
        City city = address.getCity();
        City savedCity = cityService.getCityById(city.getId());
        if (!city.equals(savedCity)) {
            throw new IllegalArgumentException("City's data does not match with the one in database.");
        }
    }

    private void checkIfVenueAddressExists(UUID id) {
        if (!venueAddressRepository.existsById(id)) {
            throw new NoSuchElementException("Address not found by given id.");
        }
    }

    // logic

    public ResponseEntity<String> createVenueAddress(VenueAddress venueAddress) {
        checkIfCityMatchesInDatabase(venueAddress);
        venueAddressRepository.save(venueAddress);
        return ResponseEntity.status(HttpStatus.CREATED).body("VenueAddress successfully created.");
    }

    public ResponseEntity<String> updateVenueAddress(VenueAddress venueAddress) {
        checkIfCityMatchesInDatabase(venueAddress);
        checkIfVenueAddressExists(venueAddress.getId());
        venueAddressRepository.save(venueAddress);
        return ResponseEntity.status(HttpStatus.OK).body("VenueAddress successfully updated.");
    }
}
