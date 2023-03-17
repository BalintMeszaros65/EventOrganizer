package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.exception.CustomExceptions;
import com.codecool.eventorganizer.model.AppUser;
import com.codecool.eventorganizer.model.Event;
import com.codecool.eventorganizer.model.Performance;
import com.codecool.eventorganizer.model.Venue;
import com.codecool.eventorganizer.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final VenueService venueService;
    private final PerformanceService performanceService;
    private final AppUserService appUserService;

    @Autowired
    public EventService(EventRepository eventRepository, VenueService venueService, PerformanceService performanceService, AppUserService appUserService) {
        this.eventRepository = eventRepository;
        this.venueService = venueService;
        this.performanceService = performanceService;
        this.appUserService = appUserService;
    }

    // basic CRUD operations
    public void saveAndUpdateEvent(Event event) {
        eventRepository.save(event);
    }

    public Event getEvent(UUID id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isPresent()) {
            return optionalEvent.get();
        } else {
            throw new NoSuchElementException("Event not found by given id.");
        }
    }

    // helper methods
    private void checkIfRequiredDataExists(Event event) {
        Venue venue = event.getVenue();
        Performance performance = event.getPerformance();
        if (venue == null || performance == null || BigDecimal.ZERO.equals(event.getBasePrice())
            || event.getTicketsSoldThroughOurApp() <= 0 || event.getEventStartingDateAndTime() == null
            || event.getEventLengthInHours() <= 0.0 || !event.isCancelled()) {
            throw new CustomExceptions.MissingAttributeException("Missing one or more attribute(s) in event.");
        }
        Venue savedVenue = venueService.getVenue(venue.getId());
        Performance savedPerformance = performanceService.getPerformanceById(performance.getId());
        if (!venue.equals(savedVenue)) {
            throw new IllegalArgumentException("Venue's data does not match with the one in database.");
        }
        if (!performance.equals(savedPerformance)) {
            throw new IllegalArgumentException("Performance's data does not match with the one in database.");
        }
    }

    private void checkIfEventExists(UUID id) {
        if (!eventRepository.existsById(id)) {
            throw new NoSuchElementException("Event not found by given id.");
        }
    }

    private void setupUpdatedEventAvailableTickets(Event savedEvent, Event updatedEvent) {
        int ticketsAlreadySold = savedEvent.getTicketsSoldThroughOurApp() - savedEvent.getAvailableTickets();
        updatedEvent.initializeTicketsToBeSold(updatedEvent.getTicketsSoldThroughOurApp(), ticketsAlreadySold);
    }

    private AppUser getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return appUserService.getUserByEmail(email);
    }

    private void checkIfCurrentUserEqualsEventOrganizer(Event event) {
        if (!getCurrentUser().equals(event.getOrganizer())) {
            throw new CustomExceptions.CurrentUserIsNotTheEventOrganizerException();
        }
    }

    // logic
    public ResponseEntity<String> createEvent(Event event) {
        checkIfRequiredDataExists(event);
        event.initializeTicketsToBeSold(event.getTicketsSoldThroughOurApp(), 0);
        event.setOrganizer(getCurrentUser());
        if (event.getId() != null) {
            throw new CustomExceptions.IdCanNotExistWhenCreatingEntityException();
        }
        saveAndUpdateEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body("Event successfully created.");
    }

    public ResponseEntity<String> updateEvent(Event event) {
        UUID id = event.getId();
        checkIfRequiredDataExists(event);
        checkIfEventExists(id);
        Event savedEvent = getEvent(id);
        setupUpdatedEventAvailableTickets(savedEvent, event);
        saveAndUpdateEvent(event);
        return ResponseEntity.status(HttpStatus.OK).body("Event successfully updated.");
    }

    // Admin role only!
    public ResponseEntity<String> deleteEvent(Event event) {
        UUID id = event.getId();
        checkIfEventExists(id);
        eventRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Event successfully deleted.");
    }

}
