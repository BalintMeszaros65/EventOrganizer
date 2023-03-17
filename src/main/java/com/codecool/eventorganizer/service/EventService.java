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
import java.time.ZonedDateTime;
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

    public Event getEventById(UUID id) {
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
        ZonedDateTime startingDateAndTime = event.getEventStartingDateAndTime();
        if (venue == null || performance == null || BigDecimal.ZERO.equals(event.getBasePrice())
            || event.getTicketsSoldThroughOurApp() <= 0 || startingDateAndTime == null
            || event.getEventLengthInHours() <= 0.0 || !event.isCancelled()) {
            throw new CustomExceptions.MissingAttributeException("Missing one or more attribute(s) in event.");
        }
        if (startingDateAndTime.isBefore(ZonedDateTime.now())) {
            throw new IllegalArgumentException("Starting date and time can not be before in the past.");
        }
        Venue savedVenue = venueService.getVenueById(venue.getId());
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

    private void setupUpdatedEventAvailableTickets(Event updatedEvent) {
        Event savedEvent = getEventById(updatedEvent.getId());
        int ticketsAlreadySold = savedEvent.getTicketsSoldThroughOurApp() - savedEvent.getAvailableTickets();
        updatedEvent.initializeTicketsToBeSold(ticketsAlreadySold);
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

    private void checkIfEventIsFullyRefundedAndCancelled(Event event) {
        Event savedEvent = getEventById(event.getId());
        if (savedEvent.getAvailableTickets() != savedEvent.getTicketsSoldThroughOurApp() || !savedEvent.isCancelled()) {
            throw new CustomExceptions.EventMustBeRefundedAndCancelledBeforeDeletingException();
        }
    }

    // logic

    public ResponseEntity<String> createEvent(Event event) {
        checkIfRequiredDataExists(event);
        event.initializeTicketsToBeSold(0);
        event.setOrganizer(getCurrentUser());
        if (event.getId() != null) {
            throw new CustomExceptions.IdCanNotExistWhenCreatingEntityException();
        }
        eventRepository.save(event);
        return ResponseEntity.status(HttpStatus.CREATED).body("Event successfully created.");
    }

    public ResponseEntity<String> updateEvent(Event event) {
        UUID id = event.getId();
        checkIfCurrentUserEqualsEventOrganizer(event);
        checkIfEventExists(id);
        checkIfRequiredDataExists(event);
        setupUpdatedEventAvailableTickets(event);
        eventRepository.save(event);
        return ResponseEntity.status(HttpStatus.OK).body("Event successfully updated.");
    }

    // Admin role only!
    public ResponseEntity<String> deleteEvent(Event event) {
        UUID id = event.getId();
        checkIfEventExists(id);
        checkIfEventIsFullyRefundedAndCancelled(event);
        eventRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Event successfully deleted.");
    }
}
