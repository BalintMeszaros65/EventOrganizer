package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.exception.CustomExceptions;
import com.codecool.eventorganizer.model.AppUser;
import com.codecool.eventorganizer.model.BookedEvent;
import com.codecool.eventorganizer.model.Event;
import com.codecool.eventorganizer.repository.BookedEventRepository;
import com.codecool.eventorganizer.utility.CreateValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class BookedEventService {
    private final BookedEventRepository bookedEventRepository;
    private final EventService eventService;
    private final AppUserService appUserService;

    @Autowired
    public BookedEventService(BookedEventRepository bookedEventRepository, EventService eventService, AppUserService appUserService) {
        this.bookedEventRepository = bookedEventRepository;
        this.eventService = eventService;
        this.appUserService = appUserService;
    }

    // basic CRUD operations

    public BookedEvent getBookedEventById(UUID id) {
        Optional<BookedEvent> optionalBookedEvent = bookedEventRepository.findById(id);
        if (optionalBookedEvent.isPresent()) {
            return optionalBookedEvent.get();
        } else {
            throw new NoSuchElementException("Booked event not found by given id.");
        }
    }

    public Set<BookedEvent> getBookedEventsByEvent(Event event) {
        return bookedEventRepository.findAllByEvent(event);
    }

    // helper methods

    private void checkIfRequiredDataExists(BookedEvent bookedEvent) {
        Event event = bookedEvent.getEvent();
        Event savedEvent = eventService.getEventById(event.getId());
        if (!event.equals(savedEvent)) {
            throw new IllegalArgumentException("Event's data does not match with the one in database.");
        }
    }

    private AppUser getCurrentUser() {
        return appUserService.getCurrentUser();
    }

    private void checkIfCurrentCustomerEqualsBookedEventCustomer(BookedEvent bookedEvent) {
        if (!getCurrentUser().equals(bookedEvent.getAppUser())) {
            throw new CustomExceptions.CurrentUserIsNotMatching(
                    "Current user does not match the user who booked the event."
            );
        }
    }

    // logic

    public BookedEvent saveBookedEvent(@Validated(CreateValidation.class) BookedEvent bookedEvent) {
        checkIfRequiredDataExists(bookedEvent);
        return bookedEventRepository.save(bookedEvent);
    }

    public void refund(BookedEvent bookedEvent) {
        checkIfCurrentCustomerEqualsBookedEventCustomer(bookedEvent);
        bookedEvent.refund();
        bookedEventRepository.save(bookedEvent);
    }

    public void refundAllByEventOrganizer(Event event) {
        Set<BookedEvent> bookedEventsToBeCancelled = getBookedEventsByEvent(event);
        bookedEventsToBeCancelled.forEach(BookedEvent::refundByEventOrganizer);
        bookedEventRepository.saveAll(bookedEventsToBeCancelled);
    }
}
