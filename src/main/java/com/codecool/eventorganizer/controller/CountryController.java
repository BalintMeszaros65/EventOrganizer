package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.model.Country;
import com.codecool.eventorganizer.service.CountryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@SecurityRequirement(name = "Basic Authentication")
@Tag(
        name = "Country",
        description = "Operations about country"
)
public class CountryController {
    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/api/country/create")
    public ResponseEntity<String> createCountry(@RequestBody Country country) {
        return countryService.createCountry(country);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/api/country/update")
    public ResponseEntity<String> updateCountry(@RequestBody Country country) {
        return countryService.updateCountry(country);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/api/country/delete/{id}")
    public ResponseEntity<String> deleteCountry(@PathVariable UUID id) {
        return countryService.deleteCountry(id);
    }

    @Secured({"ROLE_ORGANIZER", "ROLE_ADMIN"})
    @GetMapping("/api/country/get-all")
    public List<Country> getAllCountry() {
        return countryService.getAllCountry();
    }
}
