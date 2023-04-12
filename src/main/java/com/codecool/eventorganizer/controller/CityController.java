package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.model.City;
import com.codecool.eventorganizer.model.Country;
import com.codecool.eventorganizer.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/api/city/create")
    public ResponseEntity<String> createCity(@RequestBody City city) {
        return cityService.createCity(city);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/api/city/update")
    public ResponseEntity<String> updateCity(@RequestBody City city) {
        return cityService.updateCity(city);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/api/city/delete/{id}")
    public ResponseEntity<String> deleteCity(@PathVariable UUID id) {
        return cityService.deleteCity(id);
    }

    @Secured({"ROLE_ORGANIZER", "ROLE_ADMIN"})
    @GetMapping("/api/city/get-all-by-country")
    public List<City> getAllCityByCountry(@RequestBody Country country) {
        return cityService.getAllCityByCountry(country);
    }
}