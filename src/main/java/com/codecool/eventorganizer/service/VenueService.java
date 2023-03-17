package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.exception.CustomExceptions;
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

    @Autowired
    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    // basic CRUD operations

    public void saveAndUpdateVenue(Venue venue) {
        venueRepository.save(venue);
    }

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

    private static void checkIfRequiredDataExists(Venue venue) {
        int capacity = venue.getCapacity();
        String name = venue.getName();
        String country = venue.getCountry();
        String city = venue.getCity();
        String zipCode = venue.getZipCode();
        String street  = venue.getStreet();
        String house = venue.getHouse();
        if (capacity <= 0 || name == null || country == null || city == null || zipCode == null || street == null
            || house == null || "".equals(name) || "".equals(country) || "".equals(city) || "".equals(zipCode)
                || "".equals(street) || "".equals(house)) {
            throw new CustomExceptions.MissingAttributeException("Missing one or more attribute(s) in venue.");
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

    public ResponseEntity<String> deleteVenue(Venue venue) {
        UUID id = venue.getId();
        checkIfVenueExists(id);
        venueRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Venue successfully deleted.");
    }
}
