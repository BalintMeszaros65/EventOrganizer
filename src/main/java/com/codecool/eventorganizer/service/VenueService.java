package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.model.Venue;
import com.codecool.eventorganizer.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class VenueService {
    private final VenueRepository venueRepository;

    @Autowired
    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    public void saveAndUpdateVenue(Venue venue) {
        venueRepository.save(venue);
    }

    public Venue getVenue(UUID id) {
        Optional<Venue> optionalVenue = venueRepository.findById(id);
        if (optionalVenue.isPresent()) {
            return optionalVenue.get();
        } else {
            throw new NoSuchElementException("Venue not found by given id.");
        }
    }

    public void deleteVenue(UUID id) {
        venueRepository.deleteById(id);
    }
}
