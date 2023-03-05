package com.codecool.eventorganizer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

// TODO check in service if ticketsSoldThroughOurApp > Venue.capacity and throw error if true
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
    private LocalDateTime eventDateAndTime;
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

    public LocalDateTime getEventDateAndTime() {
        return eventDateAndTime;
    }

    public void initializeTicketsToBeSold(int ticketsSoldThroughOurApp) throws Exception {
        if (venue.getCapacity() >= ticketsSoldThroughOurApp) {
            this.ticketsSoldThroughOurApp = ticketsSoldThroughOurApp;
            availableTickets = ticketsSoldThroughOurApp;
        } else {
            // TODO make custom exception(s)
            throw new Exception();
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

    public void bookTicket(int ticketCount) throws Exception {
        if (canBeBooked(ticketCount)) {
            availableTickets -= ticketCount;
        } else {
            // TODO make custom exception(s)
            throw new Exception();
        }
    }

    public void refundTicket(int ticketCount) throws Exception {
        if (canBeRefunded()) {
            availableTickets += ticketCount;
        } else {
            // TODO make custom exception(s)
            throw new Exception();
        }
    }

    public boolean canBeBooked(int ticketCount) {
        // TODO ask if LocalDate works as I think
        return availableTickets >= ticketCount && eventDateAndTime.minusDays(daysBeforeBookingIsClosed).isBefore(LocalDateTime.now());
    }

    public boolean canBeRefunded() {
        return venue.isThereRefund() && eventDateAndTime.minusDays(daysBeforeBookingIsClosed).isBefore(LocalDateTime.now());
    }
}
