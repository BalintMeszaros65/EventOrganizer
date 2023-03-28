package com.codecool.eventorganizer.model;

import com.codecool.eventorganizer.exception.CustomExceptions;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
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
    // storing genre in case event or performance gets deleted,
    // so that the suggestions algorithm will be able to still use it
    @ManyToOne
    @NotNull
    private Genre genre;
    @NotNull
    private boolean isRefunded;

    public BookedEvent(Event event, BigDecimal amountPayed, int ticketCount, AppUser appUser) {
        this.event = event;
        this.amountPayed = amountPayed;
        this.ticketCount = ticketCount;
        this.appUser = appUser;
        this.dateOfBooking = ZonedDateTime.now();
        this.genre = event.getPerformance().getGenre();
        this.isRefunded = false;
    }

    public BookedEvent() {

    }

    public Genre getGenre() {
        return genre;
    }

    public UUID getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public BigDecimal getAmountPayed() {
        return amountPayed;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public ZonedDateTime getDateOfBooking() {
        return dateOfBooking;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public boolean canBeRefunded() {
        return event.canBeRefunded() && !isRefunded;
    }

    public boolean isRefunded() {
        return isRefunded;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
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
}
