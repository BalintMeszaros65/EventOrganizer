package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.exception.CustomExceptions;
import com.codecool.eventorganizer.model.Address;
import com.codecool.eventorganizer.model.Venue;
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
        int capacity = venue.getCapacity();
        String name = venue.getName();
        Address address = venue.getAddress();
        if (capacity <= 0 || name == null || "".equals(name) || address == null) {
            throw new CustomExceptions.MissingAttributeException("Missing one or more attribute(s) in venue.");
        }
        Address savedAddress = addressService.getAddressById(address.getId());
        if (!address.equals(savedAddress)) {
            throw new IllegalArgumentException("Address' data does not match with the one in database.");
        }
    }

    // logic

    public ResponseEntity<String> createVenue(Venue venue) {
        checkIfRequiredDataExists(venue);
        if (venue.getId() != null) {
            throw new CustomExceptions.IdCanNotExistWhenCreatingEntityException();
        }
        venueRepository.save(venue);
        return ResponseEntity.status(HttpStatus.CREATED).body("Venue successfully created.");
    }

    public ResponseEntity<String> updateVenue(Venue venue) {
        checkIfRequiredDataExists(venue);
        checkIfVenueExists(venue.getId());
        venueRepository.save(venue);
        return ResponseEntity.status(HttpStatus.OK).body("Venue successfully updated.");
    }

    // TODO implement logic to handle booked events (already done and upcoming)
    // TODO OR implement inactive flag to deactivate non functioning Venues
    // TODO ask if there is a way of deleting enitity while keeping it historically in other entities
    public ResponseEntity<String> deleteVenue(UUID id) {
        checkIfVenueExists(id);
        venueRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Venue successfully deleted.");
    }
}
