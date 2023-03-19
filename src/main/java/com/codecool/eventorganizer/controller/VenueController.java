package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.model.Venue;
import com.codecool.eventorganizer.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class VenueController {
    private final VenueService venueService;

    @Autowired
    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/api/venue/create")
    public ResponseEntity<String> createVenue(@RequestBody Venue venue) {
        return venueService.createVenue(venue);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/api/venue/update")
    public ResponseEntity<String> updateVenue(@RequestBody Venue venue) {
        return venueService.updateVenue(venue);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/api/venue/delete/{id}")
    public ResponseEntity<String> deleteVenue(@PathVariable UUID id) {
        return venueService.deleteVenue(id);
    }
}
