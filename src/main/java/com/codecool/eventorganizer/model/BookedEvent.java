package com.codecool.eventorganizer.model;

import com.codecool.eventorganizer.exception.CustomExceptions;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class BookedEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    @ManyToOne
    private Event event;
    @NotNull
    private BigDecimal amountPayed;
    @NotNull
    private int ticketCount;
    @NotNull
    private ZonedDateTime dateOfBooking;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private AppUser appUser;
    @NotNull
    private boolean isRefunded;

    public BookedEvent(Event event, BigDecimal amountPayed, int ticketCount, AppUser appUser) {
        this.event = event;
        this.amountPayed = amountPayed;
        this.ticketCount = ticketCount;
        this.appUser = appUser;
        this.dateOfBooking = ZonedDateTime.now();
        this.isRefunded = false;
    }

    public Genre getGenre() {
        return event.getGenre();
    }

    public boolean canBeRefunded() {
        return event.canBeRefunded() && !isRefunded;
    }

    public void refund() {
        if (!canBeRefunded()) {
            throw new CustomExceptions.IllegalEventStateException("Booked event can not be refunded.");
        }
        // TODO payment later?
        isRefunded = true;
    }

    public void refundByEventOrganizer() {
        if (!isRefunded) {
            // TODO payment later?
            isRefunded = true;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookedEvent bookedEvent)) return false;
        return ticketCount == bookedEvent.ticketCount && isRefunded == bookedEvent.isRefunded
                && Objects.equals(id, bookedEvent.id)
                && Objects.equals(event, bookedEvent.event) && Objects.equals(amountPayed, bookedEvent.amountPayed)
                && Objects.equals(dateOfBooking, bookedEvent.dateOfBooking)
                && Objects.equals(appUser, bookedEvent.appUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, event, amountPayed, ticketCount, dateOfBooking, appUser, isRefunded);
    }
}
