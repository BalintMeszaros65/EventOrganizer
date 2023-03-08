package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.model.Venue;
import com.codecool.eventorganizer.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class VenueController {
    private final VenueService venueService;

    @Autowired
    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @PostMapping("/api/venue/create")
    public ResponseEntity<String> createVenue(@RequestBody Venue venue) {
        return venueService.createVenue(venue);
    }


}
