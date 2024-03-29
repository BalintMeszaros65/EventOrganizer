package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.model.Venue;
import com.codecool.eventorganizer.model.VenueAddress;
import com.codecool.eventorganizer.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class VenueService {
    private final VenueRepository venueRepository;
    private final AddressService addressService;

    @Autowired
    public VenueService(VenueRepository venueRepository, AddressService addressService) {
        this.venueRepository = venueRepository;
        this.addressService = addressService;
    }

    // basic CRUD operations

    public Venue getVenueById(UUID id) {
        Optional<Venue> optionalVenue = venueRepository.findById(id);
        if (optionalVenue.isPresent()) {
            return optionalVenue.get();
        } else {
            throw new NoSuchElementException("Venue not found by given id.");
        }
    }

    // helper methods

    private void checkIfVenueExists(UUID id) {
        if (!venueRepository.existsById(id)) {
            throw new NoSuchElementException("Venue not found by given id.");
        }
    }

    private void checkIfRequiredDataExists(Venue venue) {
        if (venue.isInactive()) {
            throw new IllegalArgumentException("Venue can not be inactive when creating/updating.");
        }
        VenueAddress venueAddress = venue.getVenueAddress();
        VenueAddress savedVenueAddress = addressService.getVenueAddressById(venueAddress.getId())  ;
        if (!venueAddress.equals(savedVenueAddress)) {
            throw new IllegalArgumentException("VenueAddress' data does not match with the one in database.");
        }
    }

    // logic

    public ResponseEntity<String> createVenue(Venue venue) {
        checkIfRequiredDataExists(venue);
        venueRepository.save(venue);
        return ResponseEntity.status(HttpStatus.CREATED).body("Venue successfully created.");
    }

    public ResponseEntity<String> updateVenue(Venue venue) {
        checkIfRequiredDataExists(venue);
        checkIfVenueExists(venue.getId());
        venueRepository.save(venue);
        return ResponseEntity.status(HttpStatus.OK).body("Venue successfully updated.");
    }

    public ResponseEntity<String> activateVenue(UUID id) {
        Venue venue = getVenueById(id);
        venue.setInactive(false);
        venueRepository.save(venue);
        return ResponseEntity.status(HttpStatus.OK).body("Venue successfully activated.");
    }

    public ResponseEntity<String> inactivateVenue(UUID id) {
        Venue venue = getVenueById(id);
        venue.setInactive(true);
        venueRepository.save(venue);
        return ResponseEntity.status(HttpStatus.OK).body("Venue successfully inactivated.");
    }
}
