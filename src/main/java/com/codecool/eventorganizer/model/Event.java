package com.codecool.eventorganizer.model;

import com.codecool.eventorganizer.exception.CustomExceptions;
import com.codecool.eventorganizer.utility.CreateValidation;
import com.codecool.eventorganizer.utility.UpdateValidation;
import com.codecool.eventorganizer.utility.ZonedDateTimeFormatter;
import jakarta.persistence.*;
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
    @Null(groups = CreateValidation.class, message = "Id must not exist when creating.")
    @NotNull(groups = UpdateValidation.class)
    private UUID id;
    @NotNull
    @ManyToOne
    private Venue venue;
    @NotNull
    @ManyToOne
    private Performance performance;
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
    @AssertFalse(groups = {CreateValidation.class, UpdateValidation.class})
    private boolean isCancelled;

    public Genre getGenre() {
        return performance.getGenre();
    }

    public City getCity() {
        return venue.getCity();
    }

    public Country getCounty() {
        return venue.getCountry();
    }

    public void cancel() {
        if (isCancelled) {
            throw new CustomExceptions.IllegalEventStateException(
                    "Can not cancel an event, that has already been cancelled."
            );
        }
        isCancelled = true;
    }

    public void initializeAvailableTickets(int ticketsAlreadySold) {
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
        if (ticketCount <= 0) {
            throw new IllegalArgumentException("Can not calculate price for 0 or negative number of tickets.");
        }
        BigDecimal price = BigDecimal.ZERO;
        for (int i = 0; i < ticketCount; i++) {
            if (availableTickets - i > ticketsSoldThroughOurApp * 0.9) {
                // super early bird price
                price = price.add(basePrice.multiply(BigDecimal.valueOf(0.8)));
            } else if (availableTickets - i > ticketsSoldThroughOurApp * 0.8) {
                // early bird price
                price = price.add(basePrice.multiply(BigDecimal.valueOf(0.9)));
            } else {
                // regular price
                price = price.add(basePrice);
            }
        }
        return price;
    }

    public void bookTickets(int ticketCount) {
        if (ticketCount <= 0) {
            throw new IllegalArgumentException("Can not buy 0 or negative number of tickets.");
        }
        if (isCancelled) {
            throw new CustomExceptions.IllegalEventStateException("Cancelled event can not be booked.");
        }
        ZonedDateTime endOfBooking = eventStartingDateAndTime.minusDays(daysBeforeBookingIsClosed);
        if (endOfBooking.isBefore(ZonedDateTime.now())) {
            throw new CustomExceptions.IllegalEventStateException(String.format("Booking for event ended at %s",
                    endOfBooking.format(ZonedDateTimeFormatter.formatter)));
        }
        if (availableTickets < ticketCount)
            throw new CustomExceptions.IllegalEventStateException(String.format("Not enough tickets left (%s)",
                    availableTickets));
        availableTickets -= ticketCount;
    }

    public void refundTickets(int ticketCount) {
        if (ticketCount <= 0) {
            throw new IllegalArgumentException("Can not refund 0 or negative number of tickets.");
        }
        if (isCancelled) {
            throw new CustomExceptions.IllegalEventStateException("Cancelled event can not be refunded.");
        }
        if (!venue.isThereRefund()) {
            throw new CustomExceptions.IllegalEventStateException("Refunding is not allowed by the venue.");
        }
        ZonedDateTime endOfRefunding = eventStartingDateAndTime.minusDays(daysBeforeBookingIsClosed);
        if (endOfRefunding.isBefore(ZonedDateTime.now())) {
            throw new CustomExceptions.IllegalEventStateException(
                    String.format("Refunding for event ended at %s",
                            endOfRefunding.format(ZonedDateTimeFormatter.formatter)));
        }
        availableTickets += ticketCount;
    }

    public boolean canBeRefunded() {
        return venue.isThereRefund() && !isCancelled &&
                ZonedDateTime.now().isBefore(eventStartingDateAndTime.minusDays(daysBeforeBookingIsClosed));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event event)) return false;
        return getTicketsSoldThroughOurApp() == event.getTicketsSoldThroughOurApp()
                && getAvailableTickets() == event.getAvailableTickets()
                && getEventLengthInMinutes() == event.getEventLengthInMinutes()
                && getDaysBeforeBookingIsClosed() == event.getDaysBeforeBookingIsClosed()
                && isCancelled() == event.isCancelled() && Objects.equals(getId(), event.getId())
                && Objects.equals(getVenue(), event.getVenue()) && Objects.equals(getPerformance(), event.getPerformance())
                && Objects.equals(getOrganizer(), event.getOrganizer()) && Objects.equals(getBasePrice(), event.getBasePrice())
                && Objects.equals(getEventStartingDateAndTime(), event.getEventStartingDateAndTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getVenue(), getPerformance(), getOrganizer(), getBasePrice(),
                getTicketsSoldThroughOurApp(), getAvailableTickets(), getEventStartingDateAndTime(),
                getEventLengthInMinutes(), getDaysBeforeBookingIsClosed(), isCancelled());
    }
}
