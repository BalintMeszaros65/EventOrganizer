package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.service.BookingAndRefundingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class BookingAndRefundingController {
    private final BookingAndRefundingService bookingAndRefundingService;

    @Autowired
    public BookingAndRefundingController(BookingAndRefundingService bookingAndRefundingService) {
        this.bookingAndRefundingService = bookingAndRefundingService;
    }

    @PostMapping("/api/book-event/{event_id}/{ticket_count}")
    public ResponseEntity<String> bookEvent(@PathVariable("event_id") UUID eventId, @PathVariable("ticket_count") int ticketCount) {
        return bookingAndRefundingService.bookEvent(eventId, ticketCount);
    }

    @PutMapping("/api/event/{event_id}/cancel")
    public ResponseEntity<String> cancelEvent(@PathVariable("event_id") UUID eventId) {
        return bookingAndRefundingService.cancelEvent(eventId);
    }
}
