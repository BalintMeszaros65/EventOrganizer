package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.exception.CustomExceptions;
import com.codecool.eventorganizer.model.Country;
import com.codecool.eventorganizer.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class CountryService {
    CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    // basic CRUD operations

    public Country getCountryById(UUID id) {
        Optional<Country> optionalCountry = countryRepository.findById(id);
        if (optionalCountry.isPresent()) {
            return optionalCountry.get();
        } else {
            throw new NoSuchElementException("Country not found by given id.");
        }
    }

    public List<Country> getAllCountry() {
        return countryRepository.findAll();
    }

    // helper methods

    private static void checkIfRequiredDataExists(Country country) {
        String name = country.getName();
        if (name == null || "".equals(name)) {
            throw new CustomExceptions.MissingAttributeException("Missing one or more attribute(s) in country.");
        }
    }

    private void checkIfCountryExists(UUID id) {
        if (!countryRepository.existsById(id)) {
            throw new NoSuchElementException("Country not found by given id.");
        }
    }

    // logic

    public ResponseEntity<String> createCountry(Country country) {
        checkIfRequiredDataExists(country);
        if (country.getId() != null) {
            throw new CustomExceptions.IdCanNotExistWhenCreatingEntityException();
        }
        countryRepository.save(country);
        return ResponseEntity.status(HttpStatus.CREATED).body("Country successfully created.");
    }

    public ResponseEntity<String> updateCountry(Country country) {
        checkIfRequiredDataExists(country);
        checkIfCountryExists(country.getId());
        countryRepository.save(country);
        return ResponseEntity.status(HttpStatus.OK).body("Country successfully updated.");
    }

    public ResponseEntity<String> deleteCountry(UUID id) {
        checkIfCountryExists(id);
        countryRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Country successfully deleted.");
    }
}
