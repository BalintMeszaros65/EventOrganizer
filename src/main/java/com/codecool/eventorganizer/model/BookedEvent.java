package com.codecool.eventorganizer.model;

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
    private int ticketsBought;
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

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
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

    public int getTicketsBought() {
        return ticketsBought;
    }

    public boolean canBeRefunded() {
        return event.canBeRefunded() && !isRefunded;
    }

    public boolean isRefunded() {
        return isRefunded;
    }

    public void setRefunded(boolean refunded) {
        isRefunded = refunded;
    }
}
