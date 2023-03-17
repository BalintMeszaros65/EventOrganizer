package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.service.BookingAndRefundingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
