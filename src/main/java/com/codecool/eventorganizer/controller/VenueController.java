package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.model.Venue;
import com.codecool.eventorganizer.service.VenueService;
import com.codecool.eventorganizer.utility.CreateValidation;
import com.codecool.eventorganizer.utility.UpdateValidation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@SecurityRequirement(name = "Basic Authentication")
@Tag(
        name = "Venue",
        description = "Operations about venue"
)
public class VenueController {
    private final VenueService venueService;

    @Autowired
    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/api/venue/create")
    public ResponseEntity<String> createVenue(@Validated(CreateValidation.class) @RequestBody Venue venue) {
        return venueService.createVenue(venue);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/api/venue/update")
    public ResponseEntity<String> updateVenue(@Validated(UpdateValidation.class) @RequestBody Venue venue) {
        return venueService.updateVenue(venue);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/api/venue/switch-active-status/{id}")
    public ResponseEntity<String> activateVenue(@NotNull @PathVariable UUID id) {
        return venueService.activateVenue(id);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/api/venue/switch-active-status/{id}")
    public ResponseEntity<String> inactivateVenue(@NotNull @PathVariable UUID id) {
        return venueService.inactivateVenue(id);
    }
}
