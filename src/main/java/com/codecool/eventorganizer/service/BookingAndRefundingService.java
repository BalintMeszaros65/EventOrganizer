package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.model.AppUser;
import com.codecool.eventorganizer.model.BookedEvent;
import com.codecool.eventorganizer.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return appUserService.getCurrentUser();
    }

    // logic

    public ResponseEntity<String> bookEvent(UUID eventId, int ticketCount) {
        Event event = eventService.getEventById(eventId);
        BigDecimal amountToBePayed = event.currentPriceOfTickets(ticketCount);
        // TODO payment later?
        // tries to book tickets for event, throws error if that is not possible
        eventService.bookTickets(event, ticketCount);
        // if booking tickets was successful, creates a booked event, saves it and returns it with generated id
        BookedEvent savedBookedEvent = bookedEventService.saveBookedEvent(
                new BookedEvent(event, amountToBePayed, ticketCount, getCurrentUser())
        );
        // saves booked event in current customer's booked events list
        appUserService.addBookedEventToCurrentCustomer(savedBookedEvent);
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
        Event event = eventService.getEventById(eventId);
        // tries to cancel event
        eventService.cancelEvent(event);
        // if event was not already cancelled, refunds everyone who has not been refunded already, ignoring booking deadline
        bookedEventService.refundAllByEventOrganizer(event);
        return ResponseEntity.status(HttpStatus.OK).body("Event cancelled successfully.");
    }
}
