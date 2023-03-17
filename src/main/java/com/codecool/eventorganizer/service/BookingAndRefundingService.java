package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.model.AppUser;
import com.codecool.eventorganizer.model.BookedEvent;
import com.codecool.eventorganizer.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void bookEvent(UUID eventId, int ticketCount) {
        Event event = eventService.getEventById(eventId);
        BigDecimal amountToBePayed = event.currentPriceOfTickets(ticketCount);
        // TODO payment later?
        eventService.bookTickets(event, ticketCount);
        BookedEvent savedBookedEvent = bookedEventService.saveBookedEvent(new BookedEvent(event, amountToBePayed, ticketCount));
        appUserService.addBookedEventToCurrentUser(savedBookedEvent);
    }
}
