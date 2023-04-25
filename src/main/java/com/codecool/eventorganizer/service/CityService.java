package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.exception.CustomExceptions;
import com.codecool.eventorganizer.model.City;
import com.codecool.eventorganizer.model.Country;
import com.codecool.eventorganizer.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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

    public List<City> getAllCityByCountry(Country country) {
        return cityRepository.findAllByCountry(country);
    }

    // helper methods

    private void checkIfRequiredDataExists(City city) {
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
        checkIfRequiredDataExists(city);
        if (city.getId() != null) {
            throw new CustomExceptions.IdCanNotExistWhenCreatingEntityException();
        }
        cityRepository.save(city);
        return ResponseEntity.status(HttpStatus.CREATED).body("City successfully created.");
    }

    public ResponseEntity<String> updateCity(City city) {
        checkIfRequiredDataExists(city);
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
