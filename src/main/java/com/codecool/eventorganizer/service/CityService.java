package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.model.City;
import com.codecool.eventorganizer.model.Country;
import com.codecool.eventorganizer.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class CityService {
    private final CityRepository cityRepository;
    private final CountryService countryService;

    @Autowired
    public CityService(CityRepository cityRepository, CountryService countryService) {
        this.cityRepository = cityRepository;
        this.countryService = countryService;
    }

    // basic CRUD operations

    public City getCityById(UUID id) {
        Optional<City> optionalCity = cityRepository.findById(id);
        if (optionalCity.isPresent()) {
            return optionalCity.get();
        } else {
            throw new NoSuchElementException("City not found by given id.");
        }
    }

    public Set<City> getAllCityByCountry(Country country) {
        return cityRepository.findAllDistinctByCountry(country);
    }

    // helper methods

    private void checkIfCountryMatchesInDatabase(City city) {
        Country country = city.getCountry();
        Country savedCountry = countryService.getCountryById(country.getId());
        if (!country.equals(savedCountry)) {
            throw new IllegalArgumentException("Country's data does not match with the one in database.");
        }
    }

    private void checkIfCityExists(UUID id) {
        if (!cityRepository.existsById(id)) {
            throw new NoSuchElementException("City not found by given id.");
        }
    }

    // logic

    public ResponseEntity<String> createCity(City city) {
        checkIfCountryMatchesInDatabase(city);
        cityRepository.save(city);
        return ResponseEntity.status(HttpStatus.CREATED).body("City successfully created.");
    }

    public ResponseEntity<String> updateCity(City city) {
        checkIfCountryMatchesInDatabase(city);
        checkIfCityExists(city.getId());
        cityRepository.save(city);
        return ResponseEntity.status(HttpStatus.OK).body("City successfully updated.");
    }

    public ResponseEntity<String> deleteCity(UUID id) {
        checkIfCityExists(id);
        cityRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("City successfully deleted.");
    }
}
