package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.model.City;
import com.codecool.eventorganizer.model.Country;
import com.codecool.eventorganizer.service.CityService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@SecurityRequirement(name = "Basic Authentication")
@Tag(
        name = "City",
        description = "Operations about city"
)
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/api/city/create")
    public ResponseEntity<String> createCity(@Valid @RequestBody City city) {
        return cityService.createCity(city);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/api/city/update")
    public ResponseEntity<String> updateCity(@Valid @RequestBody City city) {
        return cityService.updateCity(city);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/api/city/delete/{id}")
    public ResponseEntity<String> deleteCity(@NotNull @PathVariable UUID id) {
        return cityService.deleteCity(id);
    }

    @Secured({"ROLE_ORGANIZER", "ROLE_ADMIN"})
    @GetMapping("/api/city/get-all-by-country")
    public Set<City> getAllCityByCountry(@Valid @RequestBody Country country) {
        return cityService.getAllCityByCountry(country);
    }
}
