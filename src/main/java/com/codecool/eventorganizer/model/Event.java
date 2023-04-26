package com.codecool.eventorganizer.model;

import com.codecool.eventorganizer.exception.CustomExceptions;
import com.codecool.eventorganizer.utility.ZonedDateTimeFormatter;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Valid
    @NotNull
    @ManyToOne
    private Venue venue;
    @Valid
    @NotNull
    @ManyToOne
    private Performance performance;
    @Valid
    @NotNull
    @ManyToOne
    private AppUser organizer;
    @DecimalMin(value = "0.0", inclusive = false)
    @NotNull
    private BigDecimal basePrice;
    @Positive
    private int ticketsSoldThroughOurApp;
    @PositiveOrZero
    private int availableTickets;
    @Future
    @NotNull
    private ZonedDateTime eventStartingDateAndTime;
    @Positive
    private int eventLengthInMinutes;
    @PositiveOrZero
    private int daysBeforeBookingIsClosed;
    // TODO add insert only validation group
    private boolean isCancelled;

    public Genre getGenre() {
        return performance.getGenre();
    }

    public void cancel() {
        if (isCancelled) {
            throw new CustomExceptions.IllegalEventStateException(
                    "Can not cancel an event, that has already been cancelled."
            );
        }
        isCancelled = true;
    }

    public void initializeTicketsToBeSold(int ticketsAlreadySold) {
        int venueCapacity = venue.getCapacity();
        if (venueCapacity < ticketsSoldThroughOurApp) {
            throw new CustomExceptions.TicketCountException(
                    String.format("Can not sell more tickets than venue's max capacity (%s)", venueCapacity)
            );

        }
        if (ticketsSoldThroughOurApp < ticketsAlreadySold) {
            throw new CustomExceptions.TicketCountException(
                    String.format("Can not sell less tickets than already sold tickets (%s)."
                            , ticketsAlreadySold)
            );
        }
        availableTickets = ticketsSoldThroughOurApp - ticketsAlreadySold;
    }

    public BigDecimal currentPriceOfTickets(int ticketCount) {
        BigDecimal currentPrice = BigDecimal.ZERO;
        for (int i = 0; i < ticketCount; i++) {
            if (availableTickets - i > ticketsSoldThroughOurApp * 0.9) {
                // super early bird price
                currentPrice = currentPrice.add(basePrice.multiply(BigDecimal.valueOf(0.8)));
            } else if (availableTickets - i > ticketsSoldThroughOurApp * 0.8) {
                // early bird price
                currentPrice = currentPrice.add(basePrice.multiply(BigDecimal.valueOf(0.9)));
            } else {
                // regular price
                currentPrice = currentPrice.add(basePrice);
            }
        }
        return currentPrice;
    }

    public void bookTickets(int ticketCount) {
        if (ticketCount <= 0) {
            throw new IllegalArgumentException("Can not buy 0 or negative number of tickets.");
        }
        if (canBeBooked(ticketCount)) {
            availableTickets -= ticketCount;
        } else {
            ZonedDateTime endOfBooking = eventStartingDateAndTime.minusDays(daysBeforeBookingIsClosed);
            throw new CustomExceptions.IllegalEventStateException(
                endOfBooking.isBefore(ZonedDateTime.now()) ?
                    String.format("Booking for event ended at %s", endOfBooking.format(ZonedDateTimeFormatter.formatter))
                    : String.format("Not enough tickets left (%s)", availableTickets)
            );
        }
    }

    public void refundTicket(int ticketCount) {
        if (ticketCount <= 0) {
            throw new IllegalArgumentException("Can not refund 0 or negative number of tickets.");
        }
        if (canBeRefunded()) {
            availableTickets += ticketCount;
        } else {
            throw new CustomExceptions.IllegalEventStateException(
                venue.isThereRefund() ?
                String.format("Refunding for event ended at %s", eventStartingDateAndTime.minusDays(daysBeforeBookingIsClosed)
                        .format(ZonedDateTimeFormatter.formatter))
                : "Refunding is not allowed by the venue."
            );
        }
    }

    public boolean canBeBooked(int ticketCount) {
        return availableTickets >= ticketCount && !isCancelled &&
            ZonedDateTime.now().isBefore(eventStartingDateAndTime.minusDays(daysBeforeBookingIsClosed));
    }

    public boolean canBeRefunded() {
        return venue.isThereRefund() && !isCancelled &&
            ZonedDateTime.now().isBefore(eventStartingDateAndTime.minusDays(daysBeforeBookingIsClosed));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event event)) return false;
        return ticketsSoldThroughOurApp == event.ticketsSoldThroughOurApp && availableTickets == event.availableTickets
                && eventLengthInMinutes == event.eventLengthInMinutes
                && daysBeforeBookingIsClosed == event.daysBeforeBookingIsClosed && isCancelled == event.isCancelled
                && Objects.equals(id, event.id) && Objects.equals(venue, event.venue)
                && Objects.equals(performance, event.performance) && Objects.equals(organizer, event.organizer)
                && Objects.equals(basePrice, event.basePrice)
                && Objects.equals(eventStartingDateAndTime, event.eventStartingDateAndTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, venue, performance, organizer, basePrice, ticketsSoldThroughOurApp, availableTickets,
                eventStartingDateAndTime, eventLengthInMinutes, daysBeforeBookingIsClosed, isCancelled);
    }
}
