package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.dto.EventDto;
import com.codecool.eventorganizer.exception.CustomExceptions;
import com.codecool.eventorganizer.model.*;
import com.codecool.eventorganizer.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
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

    public List<Event> getUpcomingEvents() {
        return eventRepository.findAllNotCancelledAfterZonedDateTime(ZonedDateTime.now());
    }

    public Event getEventById(UUID id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isPresent()) {
            return optionalEvent.get();
        } else {
            throw new NoSuchElementException("Event not found by given id.");
        }
    }

    public List<Event> getAllEventsByPerformance(Performance performance) {
        return eventRepository.findAllByPerformance(performance);
    }

    public List<Event> getAllEventsByOrganizer() {
        return eventRepository.findAllByOrganizer(getCurrentUser());
    }

    // helper methods

    private void checkIfRequiredDataExists(Event event) {
        Venue venue = event.getVenue();
        Performance performance = event.getPerformance();
        ZonedDateTime startingDateAndTime = event.getEventStartingDateAndTime();
        if (venue == null || performance == null || BigDecimal.ZERO.equals(event.getBasePrice())
                || event.getTicketsSoldThroughOurApp() <= 0 || startingDateAndTime == null
                || event.getEventLengthInMinutes() <= 0) {
            throw new CustomExceptions.MissingAttributeException("Missing one or more attribute(s) in event.");
        }
        if (event.isCancelled()) {
            throw new IllegalArgumentException("Cancelled event can not be created/updated.");
        }
        if (startingDateAndTime.isBefore(ZonedDateTime.now())) {
            throw new IllegalArgumentException("Starting date and time can not be in the past.");
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
        return appUserService.getCurrentUser();
    }

    private void checkIfCurrentUserEqualsEventOrganizer(Event event) {
        if (!getCurrentUser().equals(event.getOrganizer())) {
            throw new CustomExceptions.CurrentUserIsNotMatching(
                    "Current user does not match the user who created the event."
            );
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
        checkIfCurrentUserEqualsEventOrganizer(event);
        checkIfEventExists(event.getId());
        checkIfRequiredDataExists(event);
        setupUpdatedEventAvailableTickets(event);
        eventRepository.save(event);
        return ResponseEntity.status(HttpStatus.OK).body("Event successfully updated.");
    }

    public void refundTickets(Event event, int ticketCount) {
        event.refundTicket(ticketCount);
        eventRepository.save(event);
    }

    public ResponseEntity<String> deleteEvent(UUID id) {
        if (!getEventById(id).isCancelled()) {
            throw new CustomExceptions.IllegalEventStateException(
                    "Event must be fully refunded and cancelled before deleting it."
            );
        }
        eventRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Event successfully deleted.");
    }

    public void bookTickets(Event event, int ticketCount) {
        event.bookTickets(ticketCount);
        eventRepository.save(event);
    }

    public void cancelEvent(Event event) {
        checkIfCurrentUserEqualsEventOrganizer(event);
        event.cancel();
        eventRepository.save(event);
    }

    public List<EventDto> getUpcomingEventsForUser() {
        List<Event> upcomingEvents = getUpcomingEvents();
        AppUser currentUser = getCurrentUser();
        List<BookedEvent> bookedEvents = currentUser.getBookedEvents();
        return upcomingEvents.stream()
                .map(event -> new EventDto(event, bookedEvents))
                .toList();
    }
}
