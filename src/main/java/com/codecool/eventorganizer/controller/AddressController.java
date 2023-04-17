package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.model.VenueAddress;
import com.codecool.eventorganizer.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@SecurityRequirement(name = "Basic Authentication")
@Tag(
        name = "Address",
        description = "Operations about address"
)
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @Secured({"ROLE_ORGANIZER", "ROLE_ADMIN"})
    @PostMapping("/api/venue-address/create")
    @Operation(summary = "creates a venue address")
    public ResponseEntity<String> createVenueAddress(@RequestBody VenueAddress venueAddress) {
        return addressService.createVenueAddress(venueAddress);
    }

    @Secured({"ROLE_ORGANIZER", "ROLE_ADMIN"})
    @PutMapping("/api/venue-address/update")
    @Operation(summary = "updates a venue address")
    public ResponseEntity<String> updateVenueAddress(@RequestBody VenueAddress venueAddress) {
        return addressService.updateVenueAddress(venueAddress);
    }
}
