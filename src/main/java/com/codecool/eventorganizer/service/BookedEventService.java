package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.exception.CustomExceptions;
import com.codecool.eventorganizer.model.AppUser;
import com.codecool.eventorganizer.model.BookedEvent;
import com.codecool.eventorganizer.model.Event;
import com.codecool.eventorganizer.repository.BookedEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        int ticketsBooked = bookedEvent.getTicketCount();
        BigDecimal amountPayed = bookedEvent.getAmountPayed();
        if (event == null || bookedEvent.getAppUser() == null || amountPayed == null || BigDecimal.ZERO.equals(amountPayed)
                || ticketsBooked <= 0 || bookedEvent.getDateOfBooking() == null || bookedEvent.isRefunded()) {
            throw new CustomExceptions.MissingAttributeException("Missing one or more attribute(s) in booked event.");
        }
        if (!event.canBeBooked(ticketsBooked)) {
            throw new CustomExceptions.EventCanNotBeBookedException("Event can not be booked.");
        }
        Event savedEvent = eventService.getEventById(event.getId());
        if (!event.equals(savedEvent)) {
            throw new IllegalArgumentException("Event's data does not match with the one in database.");
        }
    }

    private AppUser getCurrentUser() {
        return appUserService.getCurrentUser();
    }

    private void checkIfCurrentUserEqualsBookedEventUser(BookedEvent bookedEvent) {
        if (!getCurrentUser().equals(bookedEvent.getAppUser())) {
            throw new CustomExceptions.CurrentUserIsNotTheOneWhoBookedTheEventException();
        }
    }

    // logic

    public BookedEvent saveBookedEvent(BookedEvent bookedEvent) {
        checkIfRequiredDataExists(bookedEvent);
        bookedEvent.setGenre(bookedEvent.getEvent().getPerformance().getGenre());
        if (bookedEvent.getId() != null) {
            throw new CustomExceptions.IdCanNotExistWhenCreatingEntityException();
        }
        return bookedEventRepository.save(bookedEvent);
    }

    public void refund(BookedEvent bookedEvent) {
        checkIfCurrentUserEqualsBookedEventUser(bookedEvent);
        bookedEvent.refund();
        bookedEventRepository.save(bookedEvent);
    }

    public void refundAllByEventOrganizer(List<BookedEvent> bookedEventsToBeCancelled) {
        bookedEventsToBeCancelled.forEach(BookedEvent::refundByEventOrganizer);
        bookedEventRepository.saveAll(bookedEventsToBeCancelled);
    }
}
