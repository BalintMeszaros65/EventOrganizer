package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.exception.CustomExceptions;
import com.codecool.eventorganizer.model.Event;
import com.codecool.eventorganizer.model.Performance;
import com.codecool.eventorganizer.model.Venue;
import com.codecool.eventorganizer.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    public EventService(EventRepository eventRepository, VenueService venueService, PerformanceService performanceService) {
        this.eventRepository = eventRepository;
        this.venueService = venueService;
        this.performanceService = performanceService;
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

    public void deleteEvent(UUID id) {
        eventRepository.deleteById(id);
    }

    // helper methods
    public void checkIfRequiredDataExists(Event event) {
        int ticketsSoldThroughOurApp = event.getTicketsSoldThroughOurApp();
        Venue venue = event.getVenue();
        Performance performance = event.getPerformance();
        if (venue == null || performance == null || BigDecimal.ZERO.equals(event.getBasePrice())
            || ticketsSoldThroughOurApp == 0 || event.getEventStartingDateAndTime() == null
            || event.getEventLengthInHours() == 0.0) {
            throw new CustomExceptions.MissingAttributeException("Missing one or more attribute(s) in event.");
        }
        int venueCapacity = venue.getCapacity();
        if (ticketsSoldThroughOurApp > venueCapacity) {
            throw new CustomExceptions.TicketCountCanNotExceedVenueCapacityException(
                String.format("Can not sell more tickets than venue's max capacity (%s)", venueCapacity)
            );
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

    // logic
    public ResponseEntity<String> createEvent(Event event) {
        checkIfRequiredDataExists(event);
        event.setAvailableTickets(event.getTicketsSoldThroughOurApp());
        eventRepository.save(event);
        return ResponseEntity.status(HttpStatus.CREATED).body("Event successfully created.");
    }
}
