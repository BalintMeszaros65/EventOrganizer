package com.codecool.eventorganizer.model;

import com.codecool.eventorganizer.exception.CustomExceptions;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    @ManyToOne
    private Venue venue;
    @NotNull
    @ManyToOne
    private Performance performance;
    @NotNull
    private BigDecimal basePrice;
    @NotNull
    private int ticketsSoldThroughOurApp;
    @NotNull
    private int availableTickets;
    @NotNull
    private ZonedDateTime eventStartingDateAndTime;
    @NotNull
    private double eventLengthInHours;
    @NotNull
    private int daysBeforeBookingIsClosed;

    public UUID getId() {
        return id;
    }

    public Venue getVenue() {
        return venue;
    }

    public Performance getPerformance() {
        return performance;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public int getTicketsSoldThroughOurApp() {
        return ticketsSoldThroughOurApp;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public double getEventLengthInHours() {
        return eventLengthInHours;
    }

    public ZonedDateTime getEventStartingDateAndTime() {
        return eventStartingDateAndTime;
    }

    public void initializeTicketsToBeSold(int ticketsSoldThroughOurApp) {
        int venueCapacity = venue.getCapacity();
        if (venueCapacity >= ticketsSoldThroughOurApp) {
            this.ticketsSoldThroughOurApp = ticketsSoldThroughOurApp;
            availableTickets = ticketsSoldThroughOurApp;
        } else {
            throw new CustomExceptions.TicketCountCanNotExceedVenueCapacityException(
                    String.format("Can not sell more tickets than venue's max capacity (%s)", venueCapacity)
            );
        }
    }

    public BigDecimal currentPriceOfTicket() {
        BigDecimal currentPrice;
        if (availableTickets > ticketsSoldThroughOurApp * 0.9) {
            // super early bird price
            currentPrice = basePrice.multiply(BigDecimal.valueOf(0.8));
        } else if (availableTickets > ticketsSoldThroughOurApp * 0.8) {
            // early bird price
            currentPrice = basePrice.multiply(BigDecimal.valueOf(0.9));
        } else {
            // regular price
            currentPrice = basePrice;
        }
        return currentPrice;
    }

    public void bookTicket(int ticketCount) {
        if (canBeBooked(ticketCount)) {
            availableTickets -= ticketCount;
        } else {
            ZonedDateTime endOfBooking = eventStartingDateAndTime.minusDays(daysBeforeBookingIsClosed);
            throw new CustomExceptions.EventCanNotBeBookedException(
                endOfBooking.isBefore(ZonedDateTime.now()) ?
                String.format("Not enough tickets left (%s)", availableTickets)
                : String.format("Booking for event ended at %s", endOfBooking)
            );
        }
    }

    public void refundTicket(int ticketCount) {
        if (canBeRefunded()) {
            availableTickets += ticketCount;
        } else {
            throw new CustomExceptions.EventCanNotBeRefundedException(
                venue.isThereRefund() ?
                String.format("Refunding for event ended at %s", eventStartingDateAndTime.minusDays(daysBeforeBookingIsClosed))
                : "Refunding is not allowed by the venue."
            );
        }
    }

    public boolean canBeBooked(int ticketCount) {
        return availableTickets >= ticketCount &&
            eventStartingDateAndTime.minusDays(daysBeforeBookingIsClosed).isBefore(ZonedDateTime.now());
    }

    public boolean canBeRefunded() {
        return venue.isThereRefund() &&
            eventStartingDateAndTime.minusDays(daysBeforeBookingIsClosed).isBefore(ZonedDateTime.now());
    }
}
