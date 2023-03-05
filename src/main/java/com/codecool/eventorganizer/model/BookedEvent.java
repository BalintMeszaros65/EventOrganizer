package com.codecool.eventorganizer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private LocalDateTime dateOfBooking;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private AppUser appUser;

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
        return event.canBeRefunded();
    }
}
