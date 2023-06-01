package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.dto.EventDtoForCreateAndUpdate;
import com.codecool.eventorganizer.dto.EventDtoForCustomer;
import com.codecool.eventorganizer.exception.CustomExceptions;
import com.codecool.eventorganizer.model.*;
import com.codecool.eventorganizer.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public Set<Event> getUpcomingEvents() {
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

    public Set<Event> getAllEventsByPerformance(Performance performance) {
        return eventRepository.findAllByPerformance(performance);
    }

    public Set<Event> getAllEventsByOrganizer(AppUser organizer) {
        return eventRepository.findAllByOrganizer(organizer);
    }

    public Set<Event> getAllEventsByCurrentOrganizer() {
        return getAllEventsByOrganizer(getCurrentUser());
    }

    // helper methods

    private void checkIfRequiredDataExists(Event event) {
        Venue venue = event.getVenue();
        Performance performance = event.getPerformance();
        if (venue.isInactive()) {
            throw new IllegalArgumentException("Event can not be created/updated with inactive venue.");
        }
        if (performance.isInactive()) {
            throw new IllegalArgumentException("Event can not be created/updated with inactive performance.");
        }
        ZonedDateTime bookingClosed = event.getEventStartingDateAndTime().minusDays(event.getDaysBeforeBookingIsClosed());
        if (bookingClosed.isBefore(ZonedDateTime.now())) {
            throw new IllegalArgumentException("Event can not be created/updated if booking is closed.");
        }
    }

    private AppUser getCurrentUser() {
        return appUserService.getCurrentUser();
    }

    private Customer getCurrentCustomer() {return appUserService.getCurrentCustomer();
    }

    private void checkIfCurrentUserEqualsEventOrganizer(Event event) {
        if (!getCurrentUser().equals(event.getOrganizer())) {
            throw new CustomExceptions.CurrentUserIsNotMatching(
                    "Current user does not match the user who created the event."
            );
        }
    }

    private void setupUpdatedEvent(EventDtoForCreateAndUpdate eventDto, Event event) {
        int ticketsAlreadySold = event.getTicketsSoldThroughOurApp() - event.getAvailableTickets();
        Venue venue = venueService.getVenueById(eventDto.getVenueId());
        Performance performance = performanceService.getPerformanceById(eventDto.getPerformanceId());
        event.setVenue(venue);
        event.setPerformance(performance);
        event.setBasePrice(eventDto.getBasePrice());
        event.setTicketsSoldThroughOurApp(eventDto.getTicketsSoldThroughOurApp());
        event.initializeAvailableTickets(ticketsAlreadySold);
        event.setEventStartingDateAndTime(eventDto.getEventStartingDateAndTime());
        event.setEventLengthInMinutes(eventDto.getEventLengthInMinutes());
        event.setDaysBeforeBookingIsClosed(eventDto.getDaysBeforeBookingIsClosed());
    }

    // logic

    public ResponseEntity<String> createEvent(EventDtoForCreateAndUpdate eventDto) {
        Venue venue = venueService.getVenueById(eventDto.getVenueId());
        Performance performance = performanceService.getPerformanceById(eventDto.getPerformanceId());
        Event event = new Event(eventDto.getId(), venue, performance, getCurrentUser(), eventDto.getBasePrice(),
                eventDto.getTicketsSoldThroughOurApp(), eventDto.getTicketsSoldThroughOurApp(), eventDto.getEventStartingDateAndTime(),
                eventDto.getEventLengthInMinutes(), eventDto.getDaysBeforeBookingIsClosed(), false);
        checkIfRequiredDataExists(event);
        eventRepository.save(event);
        return ResponseEntity.status(HttpStatus.CREATED).body("Event successfully created.");
    }

    public ResponseEntity<String> updateEvent(EventDtoForCreateAndUpdate eventDto) {
        Event event = getEventById(eventDto.getId());
        checkIfCurrentUserEqualsEventOrganizer(event);
        setupUpdatedEvent(eventDto, event);
        checkIfRequiredDataExists(event);
        eventRepository.save(event);
        return ResponseEntity.status(HttpStatus.OK).body("Event successfully updated.");
    }

    public void refundTickets(Event event, int ticketCount) {
        event.refundTickets(ticketCount);
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

    public Set<EventDtoForCustomer> getUpcomingEventsForCustomer() {
        Set<Event> upcomingEvents = getUpcomingEvents();
        Customer currentUser = getCurrentCustomer();
        Set<BookedEvent> bookedEvents = currentUser.getBookedEvents();
        return upcomingEvents.stream()
                .map(event -> new EventDtoForCustomer(event, bookedEvents))
                .collect(Collectors.toSet());
    }
}
