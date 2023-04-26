package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.service.BookingAndRefundingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@SecurityRequirement(name = "Basic Authentication")
@Tag(
        name = "Booking and refunding",
        description = "Operations about booking and refunding"
)
public class BookingAndRefundingController {
    private final BookingAndRefundingService bookingAndRefundingService;

    @Autowired
    public BookingAndRefundingController(BookingAndRefundingService bookingAndRefundingService) {
        this.bookingAndRefundingService = bookingAndRefundingService;
    }

    @Secured("ROLE_USER")
    @PostMapping("/api/book-event/{event_id}/{ticket_count}")
    public ResponseEntity<String> bookEvent(@NotNull @PathVariable("event_id") UUID eventId, @Positive @PathVariable("ticket_count") int ticketCount) {
        return bookingAndRefundingService.bookEvent(eventId, ticketCount);
    }

    @Secured("ROLE_USER")
    @PutMapping("/api/refund-booked-event/{booked_event_id}")
    public ResponseEntity<String> refundBookedEvent(@NotNull @PathVariable("booked_event_id") UUID bookedEventId) {
        return bookingAndRefundingService.refundBookedEvent(bookedEventId);
    }

    @Secured("ROLE_ORGANIZER")
    @PutMapping("/api/event/{event_id}/cancel")
    public ResponseEntity<String> cancelEvent(@NotNull @PathVariable("event_id") UUID eventId) {
        return bookingAndRefundingService.cancelEvent(eventId);
    }
}
