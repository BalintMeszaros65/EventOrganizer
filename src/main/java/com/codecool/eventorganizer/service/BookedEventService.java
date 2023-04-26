package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.exception.CustomExceptions;
import com.codecool.eventorganizer.model.AppUser;
import com.codecool.eventorganizer.model.BookedEvent;
import com.codecool.eventorganizer.model.Event;
import com.codecool.eventorganizer.repository.BookedEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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

    public List<BookedEvent> getBookedEventsByEvent(Event event) {
        return bookedEventRepository.findAllByEvent(event);
    }

    // helper methods

    private void checkIfRequiredDataExists(BookedEvent bookedEvent) {
        Event event = bookedEvent.getEvent();
        if (bookedEvent.isRefunded()) {
            throw new IllegalStateException("Booked event can not be already refunded when creating.");
        }
        Event savedEvent = eventService.getEventById(event.getId());
        if (!event.equals(savedEvent)) {
            throw new IllegalArgumentException("Event's data does not match with the one in database.");
        }
    }

    private AppUser getCurrentUser() {
        return appUserService.getCurrentUser();
    }

    private void checkIfCurrentCustomerEqualsBookedEventCustomer(BookedEvent bookedEvent) {
        // TODO: 2023. 04. 26. delete after fix
        System.out.println(getCurrentUser());
        System.out.println(bookedEvent.getAppUser());
        System.out.println(getCurrentUser().equals(getCurrentUser()));
        // TODO ask why it is not working :(
        if (!getCurrentUser().equals(bookedEvent.getAppUser())) {
            throw new CustomExceptions.CurrentUserIsNotMatching(
                    "Current user does not match the user who booked the event."
            );
        }
    }

    // logic

    public BookedEvent saveBookedEvent(BookedEvent bookedEvent) {
        checkIfRequiredDataExists(bookedEvent);
        if (bookedEvent.getId() != null) {
            throw new CustomExceptions.IdCanNotExistWhenCreatingEntityException();
        }
        return bookedEventRepository.save(bookedEvent);
    }

    public void refund(BookedEvent bookedEvent) {
        checkIfCurrentCustomerEqualsBookedEventCustomer(bookedEvent);
        bookedEvent.refund();
        bookedEventRepository.save(bookedEvent);
    }

    public void refundAllByEventOrganizer(Event event) {
        List<BookedEvent> bookedEventsToBeCancelled = getBookedEventsByEvent(event);
        bookedEventsToBeCancelled.forEach(BookedEvent::refundByEventOrganizer);
        bookedEventRepository.saveAll(bookedEventsToBeCancelled);
    }
}
