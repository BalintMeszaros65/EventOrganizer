package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.model.AppUser;
import com.codecool.eventorganizer.model.BookedEvent;
import com.codecool.eventorganizer.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class BookingAndRefundingService {
    private final AppUserService appUserService;
    private final BookedEventService bookedEventService;
    private final EventService eventService;

    @Autowired
    public BookingAndRefundingService(AppUserService appUserService, BookedEventService bookedEventService, EventService eventService) {
        this.appUserService = appUserService;
        this.bookedEventService = bookedEventService;
        this.eventService = eventService;
    }

    // helper methods

    private AppUser getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return appUserService.getUserByEmail(email);
    }

    // logic

    public ResponseEntity<String> bookEvent(UUID eventId, int ticketCount) {
        Event event = eventService.getEventById(eventId);
        BigDecimal amountToBePayed = event.currentPriceOfTickets(ticketCount);
        // TODO payment later?
        // tries to book tickets for event, throws error if that is not possible
        eventService.bookTickets(event, ticketCount);
        // if booking tickets was successful, creates a booked event, saves it and returns it with generated id
        BookedEvent savedBookedEvent = bookedEventService.saveBookedEvent(new BookedEvent(event, amountToBePayed, ticketCount));
        // saves booked event in current user's booked events list
        appUserService.addBookedEventToCurrentUser(savedBookedEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body("Event booked successfully.");
    }

    public ResponseEntity<String> refundBookedEvent(UUID bookedEventId) {
        BookedEvent bookedEvent = bookedEventService.getBookedEventById(bookedEventId);
        // tries to refund booked event, throws error if that is not possible
        bookedEventService.refund(bookedEvent);
        // if refunding was successful, "returns" refunded tickets to event
        eventService.refundTickets(bookedEvent.getEvent(), bookedEvent.getTicketCount());
        return ResponseEntity.status(HttpStatus.OK).body("Booked event refunded successfully.");
    }

    public ResponseEntity<String> cancelEvent(UUID eventId) {
        // TODO implement
        return ResponseEntity.status(HttpStatus.OK).body("Event cancelled successfully.");
    }
}
